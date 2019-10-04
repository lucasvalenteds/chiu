variable "heroku_email" {}
variable "heroku_api_key" {}
variable "heroku_app_name" {}
variable "chiu_frontend_url" {}

provider "heroku" {
  version = "~> 2.0"
  email   = "${var.heroku_email}"
  api_key = "${var.heroku_api_key}"
}

resource "heroku_app" "chiu_backend" {
  name   = "${var.heroku_app_name}"
  region = "us"

  config_vars = {
    FRONTEND_URL = "${var.chiu_frontend_url}"
    GRADLE_TASK = "shadowJar"
  }
}

resource "heroku_build" "chiu_backend" {
  app = "${heroku_app.chiu_backend.name}"
  buildpacks = ["https://github.com/heroku/heroku-buildpack-gradle.git"]

  source = {
    path = "../../chiu-backend"
  }
}

resource "heroku_addon" "chiu_database" {
  app  = "${heroku_app.chiu_backend.name}"
  plan = "heroku-redis:hobby-dev"
}

output "service_url" {
  value = "https://${heroku_app.chiu_backend.name}.herokuapp.com"
}
