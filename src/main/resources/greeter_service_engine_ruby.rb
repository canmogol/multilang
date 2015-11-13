class GreeterServiceEngineRuby
  include com.dhtd.greeter.GreeterServiceEngine

  def sayHi(name)
    'Hi Ruby ' << name
  end
end
GreeterServiceEngineRuby.new
