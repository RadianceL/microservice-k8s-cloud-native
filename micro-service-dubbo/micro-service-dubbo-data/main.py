print("first")


def say_hello():
    print("hello")
    print(__name__ + 'from hello.sayhello()')


if __name__ == "__main__":
    print('This is main of module "hello.py"')
    say_hello()
    print(__name__ + 'from hello.main')
