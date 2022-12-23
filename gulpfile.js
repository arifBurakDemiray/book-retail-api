const {task, src, dest} = require("gulp");
const mjml = require("gulp-mjml")
const zip = require("gulp-zip");
const rename = require("gulp-rename");
const filter = require("gulp-filter");
const replace = require("gulp-replace");
const chmod = require("gulp-chmod");
const cache = require("gulp-cache");

const yaml = require('js-yaml');
const fs = require('fs');
const {pathOr} = require("ramda");


// Build MJML template files and copy to target resources.
task("mjml", () => {
    return src("src/main/resources/templates/emails/*.mjml")
        .pipe(
            cache(
                mjml(),
                {
                    name: "emails",
                }
            )
        )
        .pipe(dest("target/classes/templates/emails/"))
})

// Copy artifacts for AWS CodeBuild
task("artifacts", () => {
    const properties = yaml.load(fs.readFileSync('target/classes/application.yml', 'utf8'));

    const replaceFilter = filter(".platform/**", {restore: true});
    const chmodFilter = filter("*.sh", {restore: true});

    return src(
        [
            "target/book-retail-*-server.jar",
            ".ebextensions/*.config",
            ".platform/**"
        ], {
            base: "./"
        })
        .pipe(rename((file) => {
            if (file.dirname === "target") file.dirname = "."
        }))
        .pipe(chmodFilter)
        .pipe(chmod(0o755))
        .pipe(chmodFilter.restore)
        .pipe(replaceFilter)
        .pipe(
            cache(
                replace(
                    /@(\w+(\.\w+)+)@/g,
                    (match, paths) => pathOr(match, paths.split("."), properties)
                ),
                {
                    name: "replace-filter"
                }
            )
        )
        .pipe(replaceFilter.restore)
        .pipe(dest("dist/files"));
})

// Create deployment zip
task("bundle", () => {
    return src("dist/files/**", {base: "./"})
        .pipe(zip("deployment.zip"))
        .pipe(dest("dist"));
})