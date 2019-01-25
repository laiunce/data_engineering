package com.dna.app;

import com.dna.app.config.AppConfigProps;
import com.dna.app.config.Configurable;
import com.dna.app.config.Descriptible;
import com.dna.app.config.DnaAppConfig;
import com.dna.app.config.DnaAppConstants;
import com.dna.exception.PipelineNotFoundException;
import com.dna.util.ClassUtil;
import com.dna.util.DateRange;
import static com.dna.util.DateUtil.getLocalDate;

import com.dna.util.StringUtil;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristian Laiun

 */
public class AppRunner implements Runnable {

    private static String CONFIG_FILE_NAME="dna-config.properties";

    private static final Logger LOG = LoggerFactory.getLogger(AppRunner.class);

    private final List<Runnable> runners;
    private final boolean isConcurrent;
    private String runnerName = "";
    private String runnerDescription = "";

    public AppRunner(List<Runnable> runners, boolean isConcurrent) {
        this.runners = runners;
        this.isConcurrent = isConcurrent;
    }

    @Override
    public void run() {
        try {
            if (isConcurrent) {
                runConcurrently();
            } else {
                runSequentially();
            }
        } catch (Exception ex) {
            LOG.error("Error on AppRunner ", ex);
            //EmailClient.sendEmailAlert(ex, runnerName, runnerDescription);
        }
    }

    private void runConcurrently() {
        final ExecutorService executor = Executors.newFixedThreadPool(10);
        for (Runnable job : runners) {
            LOG.info("********************************************************************************");
            Descriptible job2 = (Descriptible) job;
            runnerName = String.format("Executing Runner for %s", job.getClass().getName());
            runnerDescription = job2.getRunnerDescription();
            LOG.info(runnerName);
            executor.execute(job);
        }
        executor.shutdown();
        while (!executor.isTerminated())
			;
    }

    private void runSequentially() {
        for (Runnable job : runners) {
            LOG.info("********************************************************************************");
            Descriptible job2 = (Descriptible) job;
            runnerName = String.format("Executing Runner for %s", job.getClass().getName());
            runnerDescription = job2.getRunnerDescription();
            LOG.info(runnerName);
            job.run();
        }
    }

    static List<Runnable> getRunners(List<Class<Runnable>> runnerClasses) {
        try {
            List<Runnable> jobs = new ArrayList<>();
            // first get all instances to verify they all exist
            for (Class<?> clazz : runnerClasses) {
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    Runnable job = (Runnable) clazz.getConstructor().newInstance();
                    jobs.add(job);
                }
            }
            return jobs;
        } catch (Exception e) {
            throw new PipelineNotFoundException(e);
        }
    }

    public static void main(String[] args) throws Exception {

        Options options = new Options();

        Option overwriteOption = createOption(options, "o", "overwrite-data","if data will be appended or truncated (replaces all data!)", Boolean.class);
        Option replaceDataOption = createOption(options, "rd", "replace-data","if data will be replaced (deleted first)", Boolean.class);
        Option dateFromOption = createOption(options, "f", "from", "from date", LocalDate.class);
        Option dateToOption = createOption(options, "t", "to", "to date", LocalDate.class);
        Option useIdsFromAPIOption = createOption(options, "uifa", "use-ids-from-api","If true Advertiser Ids will load from the Json File", Boolean.class);
        Option concurrentOption = createOption(options, "c", "concurrent", "if process will execute concurrently",LocalDate.class);
        Option runnersOption = createOption(options, "r", "runners", "job classes to run", String.class);
        Option configFileNameOption = createOption(options, "cfn", "configFileName","filename of the .properties configuration file", String.class);
        Option paramFacebookAccountIdOverride = createOption(options, "fbaid", "facebookAccountId","Indicates that the FB Service will use this account ids instead of the ones in the properties file", String.class);
        Option paramFacebookValidateCampaignStatus = createOption(options, "fbvcs", "facebookValidateCampaignStatus","Indicates wether or not the FB Service will validate campaign status", Boolean.class);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (org.apache.commons.cli.ParseException e) {
            LOG.error("Could not parse", e);
            formatter.printHelp("DNA-Datamart", options);
            System.exit(1);
            return;
        }

        String runnerClassesString = StringUtil.toEmptyIfNull(cmd.getOptionValue(runnersOption.getLongOpt())).replaceAll("\\s", "");

        String importOverwriteStr = cmd.getOptionValue(overwriteOption.getLongOpt());
        Boolean importOverwrite = importOverwriteStr != null ? Boolean.parseBoolean(importOverwriteStr) : null;
        String replaceDataOptionStr = cmd.getOptionValue(replaceDataOption.getLongOpt());
        Boolean replaceData = replaceDataOptionStr != null ? Boolean.parseBoolean(replaceDataOptionStr) : null;

        Boolean isConcurrent = Boolean.parseBoolean(cmd.getOptionValue(concurrentOption.getLongOpt()));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

        LocalDate dateFrom = getLocalDate(cmd.getOptionValue(dateFromOption.getLongOpt()), dateTimeFormatter);
        LocalDate dateTo = getLocalDate(cmd.getOptionValue(dateToOption.getLongOpt()), dateTimeFormatter);

        if (dateTo != null && dateFrom == null) {
            dateFrom = dateTo;
        } else if (dateFrom != null && dateTo == null) {
            dateTo = LocalDate.now();
        }

        //LOG.info("Running date range from '{}' to '{}'", dateFrom.toString(), dateTo.toString());

        String useAdIdsJsonOptionStr = cmd.getOptionValue(useIdsFromAPIOption.getLongOpt());
        Boolean useIdsFromAPI = useAdIdsJsonOptionStr != null ? Boolean.parseBoolean(useAdIdsJsonOptionStr) : Boolean.FALSE;

        AppRunner.CONFIG_FILE_NAME = StringUtil.toEmptyIfNull(cmd.getOptionValue(configFileNameOption.getLongOpt())).replaceAll("\\s", "");
        
        LOG.info(String.format("Configuration filename: %s", AppRunner.CONFIG_FILE_NAME));

        DnaAppConstants.setAppConfigProps(new AppConfigProps(AppRunner.CONFIG_FILE_NAME));
        LOG.info(String.format("Configuration project.id: %s", DnaAppConstants.getProjectId()));

        List<Class<Runnable>> runnerClasses = !StringUtil.isEmpty(runnerClassesString)
                ? ClassUtil.getClasses(runnerClassesString.split(","), Runnable.class)
                : DnaAppConstants.getAppConfigProps().getClasses("runners", Runnable.class);

        Boolean fbvcs
                = cmd.getOptionValue(paramFacebookValidateCampaignStatus.getLongOpt()) != null
                ? Boolean.parseBoolean(cmd.getOptionValue(paramFacebookValidateCampaignStatus.getLongOpt()))
                : null;

        DnaAppConfig appConfig = new DnaAppConfig.Builder(DnaAppConstants.getAppConfigProps())
                .setImportOverwrite(importOverwrite).setReplaceData(replaceData)
                .setUseIdsFromAPI(useIdsFromAPI)
                .setDateRange(new DateRange(dateFrom, dateTo))
                .setFacebookAccountIdsOverride(
                        cmd.getOptionValue(
                                paramFacebookAccountIdOverride.getLongOpt()
                        )
                )
                .setFacebookValidateCampaignStatus(fbvcs)
                .build();

        List<Runnable> runners = getRunners(runnerClasses);
        for (Runnable runner : runners) {
            if (runner instanceof Configurable) {
                Configurable.class.cast(runner).setAppConfig(appConfig);
            }
        }

        new AppRunner(runners, isConcurrent).run();
    }

    private static Option createOption(Options options, String shortName, String longName, String description,
            Class<?> type) {
        Option dateToOption = new Option(shortName, longName, true, description);
        dateToOption.setRequired(false);
        dateToOption.setType(type);
        options.addOption(dateToOption);
        return dateToOption;
    }
}
