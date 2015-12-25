class RubyResource
  include com.dhtd.restful.RubyResource

  def sayHi(name)
    puts(' >>> RubyResource name: ' << name)
    'Hi Ruby ' << name
  end
end
RubyResource.new