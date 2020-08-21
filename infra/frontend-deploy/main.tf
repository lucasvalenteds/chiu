variable "heroku_email" {}
variable "heroku_api_key" {}
variable "heroku_app_name" {}

provider "heroku" {
  version = "~> 2.0"
  email   = var.heroku_email
  api_key = var.heroku_api_key
}

resource "heroku_app" "chiu_frontend" {
  name   = var.heroku_app_name
  region = "us"
}

resource "heroku_build" "chiu_frontend" {
  app = heroku_app.chiu_frontend.name
  buildpacks = [
    "https://github.com/heroku/heroku-buildpack-nodejs.git"
  ]

  source = {
    path = "../../chiu-frontend"
  }
}

output "service_url" {
  value = "https://${heroku_app.chiu_frontend.name}.herokuapp.com"
}
