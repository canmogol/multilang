<?
use com\dhtd\greeter\GreeterServiceEngine as GreeterServiceEngine;

class GreeterServiceEnginePhp implements GreeterServiceEngine
{

    public function sayHi($name)
    {
        echo " >>> GreeterServiceEnginePhp name: " . $name;
        return "Groovy " . $name;
    }

}

new GreeterServiceEnginePhp();

