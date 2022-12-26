package com.bookretail.service;

import com.bookretail.config.security.JwtUtil;
import com.bookretail.dto.Response;
import com.bookretail.dto.auth.ProfileDto;
import com.bookretail.dto.message.BasicResponse;
import com.bookretail.dto.user.ProfilePictureUpdateDto;
import com.bookretail.enums.EErrorCode;
import com.bookretail.factory.UserFactory;
import com.bookretail.repository.UserRepository;
import com.bookretail.util.ImageUtils;
import com.bookretail.util.service.storage.IStorageService;
import com.bookretail.validator.UserValidator;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserFactory userFactory;
    private final JwtUtil jwtTokenUtil;
    private final IStorageService storageService;
    private final MessageSourceAccessor messageSource;

    @Transactional(readOnly = true)
    public Response<ProfileDto> getProfile(@NotNull String token) {
        var user = userRepository.getById(jwtTokenUtil.getUserId(token));

        return Response.ok(userFactory.createProfileDto(user));
    }

    @Transactional
    public Response<ProfilePictureUpdateDto> updateProfilePicture(String token, MultipartFile file) {
        var userId = jwtTokenUtil.getUserId(token);
        var validation = userValidator.validate(file);

        if (validation.isNotValid()) {
            return Response.notOk(validation.getMessage(), EErrorCode.BAD_REQUEST);
        }

        var maybeUser = userRepository.findById(userId);

        if (maybeUser.isEmpty()) {
            return Response.notOk(messageSource.getMessage("update_profile_picture.user.not_found"),
                    EErrorCode.BAD_REQUEST);
        }

        var user = maybeUser.get();

        // Resize image and convert to InputStream.
        URL previousUrl = null;
        try {
            previousUrl = new URL(user.getProfilePicture());
        } catch (MalformedURLException ignored) {
            // This exception is tenable because removing old data is not relevant to user.
        }

        try {
            var image = ImageUtils.resizeImage(
                    ImageIO.read(file.getInputStream()),
                    128, 128
            );
            var is = ImageUtils.toInputStream(image);

            // Upload image.
            var url = storageService.put(is).toExternalForm();

            // Update profile picture.
            user.setProfilePicture(url);
            userRepository.save(user);

            // Delete previous picture if it is valid.
            if (previousUrl != null) {
                storageService.delete(previousUrl);
            }

            return Response.ok(new ProfilePictureUpdateDto(url));
        } catch (IOException e) {
            return Response.notOk(messageSource
                    .getMessage("update_profile_picture.io.fail"), EErrorCode.UNHANDLED);
        }
    }

    @Transactional
    public Response<BasicResponse> depositMoney(String token, Double amount) {

        if (amount <= 0) {
            return Response.notOk(messageSource.getMessage("validation.generic.number.positive"),
                    EErrorCode.BAD_REQUEST);
        }

        var user = userRepository.getById(jwtTokenUtil.getUserId(token));
        user.setMoney(amount + user.getMoney());
        userRepository.save(user);


        return Response.ok(new BasicResponse(messageSource.getMessage("update.user.money.success")));
    }
}
