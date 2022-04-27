NAME = grpc-beer-gateway

default: build

image:
	@docker build -t $(NAME) .

build:
	@buf generate
	@go build 

clean:
	@rm -f $(NAME)