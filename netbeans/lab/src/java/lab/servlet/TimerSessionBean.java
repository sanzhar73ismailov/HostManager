package lab.servlet;

import instrument.InstrumentIndicator;
import java.util.Date;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

@Singleton
public class TimerSessionBean {

    int counter;

   //Автоматический запуск всех драйверов каждый час (делается лишь один раз)    
   //@Schedule(dayOfWeek = "*", month = "*", hour = "*/1", dayOfMonth = "*", year = "*", minute = "0", second = "0")
   //Автоматический запуск всех драйверов каждые 30 мин (делается лишь один раз)
    @Schedule(dayOfWeek = "*", month = "*", hour = "*", dayOfMonth = "*", year = "*", minute = "*/30", second = "0")
    public void startAllInstruments() {
        System.out.println("started all instruments, date: " + new Date() + ", counter: " + counter++);
        InstrumentIndicator instrIndicator = instrument.InstrumentIndicator.getInstance();
        if (!instrIndicator.isAllInstrumentsStarted()) {
            System.out.println("startAllInstruments after starting Glassfish _________________");
            instrIndicator.runAllActive();
        } else {
            System.out.println("startAllInstruments already runned");
        }
    }
}
