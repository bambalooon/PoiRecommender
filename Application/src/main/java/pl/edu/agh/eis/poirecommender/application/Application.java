package pl.edu.agh.eis.poirecommender.application;

import android.os.Environment;
import de.mindpipe.android.logging.log4j.LogConfigurator;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.eis.poirecommender.R;

import java.io.File;

public class Application extends android.app.Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    @Override
    public void onCreate() {
        super.onCreate();
        initLogging(Environment.getExternalStorageDirectory() + File.separator + getPackageName() + File.separator);
        LOG.debug("{} application started.", getString(R.string.app_name));
    }

    private void initLogging(String logDirectoryPath) {
        LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(logDirectoryPath + "app.log");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(1024 * 1024 * 5);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
    }
}
