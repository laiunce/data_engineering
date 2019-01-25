package com.dna.app.config;

import com.dna.util.ClassUtil;
import com.dna.util.StringUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.reflections.Reflections;

/**
 * @author Cristian Laiun

 */
public class AppConfigProps {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    private Properties properties = null;
    private static String CONFIG_FILENAME; 

    public AppConfigProps(String configFileName) {
        try {
            CONFIG_FILENAME = configFileName;
            properties = load(configFileName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static String getConfigFilename() {
        return CONFIG_FILENAME;
    }

    public List<String> getStrings(String key) {
        String value = get(key);
        return Lists.newArrayList(Splitter.on(",").split(value));
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public long getLong(String key) {
        String prop = get(key);
        return Long.parseLong(prop);
    }

    public boolean getBoolean(String key) {
        String prop = get(key);
        return Boolean.parseBoolean(prop);
    }

    public LocalDate getLocalDate(String key) {
        String prop = get(key);
        prop = prop.toLowerCase();

        LocalDate localDate = null;

        if (prop.equals("today")) {
            localDate = LocalDate.now();
        } else if (prop.equals("yesterday")) {
            localDate = LocalDate.now().minus(1, ChronoUnit.DAYS);
        } else if (prop.endsWith(" days ago") || prop.endsWith(" day ago")) {
            Long days = Long.parseLong(prop.substring(0, prop.indexOf(" ")));
            localDate = LocalDate.now().minus(days, ChronoUnit.DAYS);
        } else {
            localDate = LocalDate.parse(prop, DATE_FORMATTER);
        }

        return localDate;
    }

    public Class<?>[] getClassesFromPackage(String key, Class<?> type, Class<? extends Annotation> filter) throws ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();

        String value = get(key);
        String[] packageNames = value.split(",");

        for (String packageName : packageNames) {
            packageName = packageName.trim();
            Reflections reflections = new Reflections(packageName);
            for (Class<?> clazz : reflections.getSubTypesOf(type)) {
                if (!clazz.isAnnotationPresent(filter)) {
                    classList.add(clazz);
                }
            }
        }

        return classList.toArray(new Class<?>[]{});
    }

    @SuppressWarnings("unchecked")
    public <T> List<Class<T>> getClasses(String key, Class<T> type) throws ClassNotFoundException {
        String classesString = get(key);
        if (StringUtil.trim(classesString).isEmpty()) return new ArrayList<>();

        String[] classNames = classesString.split(",");

        return ClassUtil.getClasses(classNames, type);
    }


    private Properties load(String configFileName) throws IOException {
        if (this.properties != null) return properties;

        this.properties = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("./" + configFileName);
            properties.load(input);
        } catch (IOException e) {

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            input = loader.getResourceAsStream(configFileName);
            properties.load(input);

        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new IOException("could not load file Config file");
            }
        }

        return properties;
    }

}
