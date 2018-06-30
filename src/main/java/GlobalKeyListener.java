import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.openqa.selenium.WebDriver;

public class GlobalKeyListener implements NativeKeyListener {

    private WebDriver driver;

    GlobalKeyListener(WebDriver driver) {
        this.driver = driver;
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                try {
                    GlobalScreen.unregisterNativeHook();
                    driver.quit();
                } catch (NativeHookException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        //System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        //System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
    }

//    public static void main(String[] args) {
//        try {
//            GlobalScreen.registerNativeHook();
//        }
//        catch (NativeHookException ex) {
//            System.err.println("There was a problem registering the native hook.");
//            System.err.println(ex.getMessage());
//
//            System.exit(1);
//        }
//
//        // Get the logger for "org.jnativehook" and set the level to off.
//        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
//        logger.setLevel(Level.OFF);
//
//        // Change the level for all handlers attached to the default logger.
//        Handler[] handlers = Logger.getLogger("").getHandlers();
//        for (Handler handler : handlers) {
//            handler.setLevel(Level.OFF);
//        }
//        // Add the appropriate listeners.
//
//        GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
//    }
}