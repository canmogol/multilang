class GreeterServiceEngineRuby
  include com.dhtd.greeter.GreeterServiceEngine

  def sayHi(name)
    # puts(' >>> GreeterServiceEngineRuby name: ' << name)
    'Hi Ruby ' << name
  end
end
GreeterServiceEngineRuby.new
