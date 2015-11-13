class RubyResource
  include com.dhtd.restful.RubyResource

  def sayHi(name)
    'Hi Ruby ' << name
  end
end
RubyResource.new