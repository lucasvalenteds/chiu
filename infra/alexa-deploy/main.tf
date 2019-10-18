variable "aws_access_key" {
}

variable "aws_secret_key" {
}

variable "aws_region" {
  default = "eu-east-1a"
}

provider "aws" {
  region = "${var.aws_region}"
}

resource "aws_iam_role" "default" {
  name = "chiu_integration_alexa_role"

  assume_role_policy = <<EOF
  {
    "Version": "2012-10-17",
    "Statement": [
      {
        "Action": "sts:AssumeRole",
        "Principal": {
          "Service": "lambda.amazonaws.com"
        },
        "Effect": "Allow",
        "Sid": ""
      }
    ]
  }
  EOF
}

resource "aws_iam_role_policy" "default" {
  name = "chiu_integration_alexa_role_policy"
  role = "${aws_iam_role.default.id}

  policy = <<EOF
  {
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Action": [
          "logs:CreateLogGroup",
          "logs:CreateLogStream",
          "logs:PutLogEvents"
        ],
        "Resource": "*"
      }
    ]
  }
  EOF
}

resource "aws_lambda_function" "current_noise_level_lambda" {
  filename = "../../chiu-integration-alexa/chiu-integration-alexa"
  function_name = "currentNoiseLevel"
  role = "${aws_iam_role.default.arn}"
  handler = "index.handlers"
  runtime = "nodejs10.x"
}

resource "aws_lambda_permission" "default" {
  statement_id = "AllowExecutionFromAlexa"
  action = "lambda:InvokeFunction"
  function_name = "${aws_lambda_function.current_noise_level_lambda.function_name}"
  principal = "alexa-appkit.amazon.com"
}

