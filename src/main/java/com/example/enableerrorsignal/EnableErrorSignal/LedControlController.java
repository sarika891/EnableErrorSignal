import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import spark.Spark;

public class LedControlController {
    public static void main(String[] args) {
        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalOutput redLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "RedLED", PinState.LOW);
        final GpioPinDigitalOutput greenLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "GreenLED", PinState.LOW);

        Spark.get("/light/:color", (req, res) -> {
            String color = req.params(":color");
            if ("red".equalsIgnoreCase(color)) {
                redLed.high();
                greenLed.low();
                return "Red light is ON";
            } else if ("green".equalsIgnoreCase(color)) {
                greenLed.high();
                redLed.low();
                return "Green light is ON";
            } else {
                redLed.low();
                greenLed.low();
                return "Invalid color. Both lights are OFF";
            }
        });

        Spark.init();
    }
}