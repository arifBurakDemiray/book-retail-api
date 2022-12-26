package com.bookretail.service;

import com.bookretail.config.security.JwtUtil;
import com.bookretail.dto.Response;
import com.bookretail.dto.user.ProfilePictureUpdateDto;
import com.bookretail.factory.UserFactory;
import com.bookretail.factory.UserTestFactory;
import com.bookretail.repository.UserRepository;
import com.bookretail.util.service.storage.IStorageService;
import com.bookretail.validator.UserValidator;
import com.bookretail.validator.ValidationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @MockBean
    private IStorageService storageService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private MessageSourceAccessor messageSourceAccessor;

    @MockBean
    private UserFactory userFactory;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @MockBean
    private UserValidator userValidator;

    @Nested
    class GetProfile_Method_Test_Cases {
        @Test
        void Test_Get_Profile() {
            //given
            var user = UserTestFactory.createSuccessTestUser_USER();
            var response = UserTestFactory.createProfileDto(user);
            var token = UserTestFactory.token_USER;

            //when
            when(jwtUtil.getUserId(token)).thenReturn(user.getId());
            when(userRepository.getById(user.getId())).thenReturn(user);
            when(userFactory.createProfileDto(user)).thenReturn(response);

            var getProfileResponse = userService.getProfile(token);
            //then
            assertTrue(getProfileResponse.isOk());
            assertEquals(user.getEmail(), getProfileResponse.getData().email);
            verify(userRepository).getById(any());
            verify(userFactory).createProfileDto(any());
            verify(jwtUtil).getUserId(any());
            verify(userRepository, times(1)).getById(user.getId());
            verifyNoMoreInteractions(userRepository);
        }
    }

    @Nested
    class Update_Profile_Picture_Method_Test_Cases {
        @Test
        @DisplayName("Test updateProfilePicture when validation.isNotValid() is true")
        void testUpdateProfilePicture() {
            //given
            var validationResult = mock(ValidationResult.class);
            var user = UserTestFactory.createSuccessTestUser_USER();
            var token = UserTestFactory.token_USER;
            //when
            when(validationResult.isNotValid()).thenReturn(true);
            when(validationResult.getMessage()).thenReturn("Validation failed");
            when(jwtUtil.getUserId(token)).thenReturn(user.getId());
            when(userValidator.validate(any())).thenReturn(validationResult);
            Response<ProfilePictureUpdateDto> updateProfilePictureRespone = userService.updateProfilePicture(token, mock(MultipartFile.class));

            assertFalse(updateProfilePictureRespone.isOk());
            verify(jwtUtil).getUserId(any());
            verify(userValidator).validate(any());

        }

        @Test
        @DisplayName("Test updateProfilePicture when maybeUser.isEmpty() is true")
        void testUpdateProfilePicture2() {
            //given
            var validationResult = mock(ValidationResult.class);
            var user = UserTestFactory.createSuccessTestUser_USER();
            var token = UserTestFactory.token_USER;
            //when
            when(validationResult.isNotValid()).thenReturn(false);
            when(messageSourceAccessor.getMessage((String) any())).thenReturn("update_profile_picture.user.not_found");
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            when(userValidator.validate(any())).thenReturn(validationResult);
            when(userRepository.findById(any())).thenReturn(Optional.empty());
            var updateProfilePictureResponse = userService.updateProfilePicture(token, mock(MultipartFile.class));

            //then
            assertFalse(updateProfilePictureResponse.isOk());
            verify(messageSourceAccessor).getMessage((String) any());
            verify(jwtUtil).getUserId(any());
            verify(userValidator).validate(any());
            verify(userRepository, times(1)).findById(user.getId());
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @DisplayName("Test updateProfilePicture when maybeUser.isEmpty() is false")
        void testUpdateProfilePicture3() throws IOException {
            //given
            var validationResult = mock(ValidationResult.class);
            var user = UserTestFactory.createSuccessTestUser_USER();
            var token = UserTestFactory.token_USER;
            var profilePhoto = UserTestFactory.profile_photo_USER;
            user.setProfilePicture(profilePhoto);
            var multipartFile = new MockMultipartFile("profile_picture.png", "profile_picture.png", "image/png",
                    "profile_picture.png".getBytes());
            var url2 = new URL(profilePhoto);

            //when
            when(validationResult.isNotValid()).thenReturn(false);
            when(messageSourceAccessor.getMessage((String) any())).thenReturn("Success");
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            when(userValidator.validate(any())).thenReturn(validationResult);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(storageService.put((InputStream) any())).thenReturn(url2);
            when(userRepository.save(any())).thenReturn(user);

            var updateProfilePictureResponse = userService.updateProfilePicture(token, multipartFile);

            //then
            assertTrue(updateProfilePictureResponse.isOk());
            verify(jwtUtil).getUserId(any());
            verify(userValidator).validate(any());
            verify(userRepository).findById(any());
            verify(storageService).put((InputStream) any());
            verify(userRepository).save(any());
            verify(storageService).delete((URL) any());
        }

        @Test
        @DisplayName("Test updateProfilePicture when maybeUser.isEmpty() is false and throw IOException " +
                "and cathcing it")
        void testUpdateProfilePicture4() throws IOException {
            //given
            var validationResult = mock(ValidationResult.class);
            var user = UserTestFactory.createSuccessTestUser_USER();
            var token = UserTestFactory.token_USER;
            var profilePhoto = UserTestFactory.profile_photo_USER;

            user.setProfilePicture(profilePhoto);
            MultipartFile multipartFile = mock(MultipartFile.class);

            //when
            when(validationResult.isNotValid()).thenReturn(false);
            when(multipartFile.getInputStream()).thenThrow(new IOException());
            when(messageSourceAccessor.getMessage((String) any())).thenReturn("Success");
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            when(userValidator.validate(any())).thenReturn(validationResult);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            var updateProfilePictureResponse = userService.updateProfilePicture(token, multipartFile);

            //then
            assertFalse(updateProfilePictureResponse.isOk());
            verify(jwtUtil).getUserId(any());
            verify(userValidator).validate(any());
            verify(userRepository).findById(any());
        }
    }
}