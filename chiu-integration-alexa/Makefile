package = chiu-integration-alexa.zip
code = index.js

default: build

build:
	@npm run compile
	@zip -r $(package) ./

clean:
	@rm $(package)
	@rm $(code)
