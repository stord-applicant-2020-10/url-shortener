.PHONY: $(MAKECMDGOALS)

# `make setup` will be used after cloning or downloading to fulfill
# dependencies, and setup the the project in an initial state.
# This is where you might download rubygems, node_modules, packages,
# compile code, build container images, initialize a database,
# anything else that needs to happen before your server is started
# for the first time
setup:
	docker build --tag url-shortener .

# `make server` will be used after `make setup` in order to start
# an http server process that listens on any unreserved port
#	of your choice (e.g. 8080). 
server:
	docker run --rm -p 127.0.0.1:9000:9000 url-shortener ./target/universal/stage/bin/stord-url-shortener

# `make test` will be used after `make setup` in order to run
# your test suite.
test:
	docker run --rm url-shortener  ./sbt test
