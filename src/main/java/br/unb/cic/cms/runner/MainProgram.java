package br.unb.cic.cms.runner;

import br.unirio.lns.hdesign.model.Project;
import br.unirio.lns.hdesign.multiobjective.ClusteringProblemBuilder;
import br.unirio.lns.hdesign.multiobjective.CouplingProblem;
import br.unirio.lns.hdesign.multiobjective.Experiment;
import br.unirio.lns.hdesign.reader.CDAFlatReader;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class MainProgram {

    private Options options;

    public static void main(String args[]) {
        MainProgram main = new MainProgram();
        try {
            main.initOptions();
            main.processOptions(args);
        }catch(org.apache.commons.cli.ParseException e) {
            final PrintWriter writer = new PrintWriter(System.out);
            HelpFormatter formatter = new HelpFormatter();
            formatter.printUsage(writer,80,"CMS Runner", main.options);
            writer.flush();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Execute the experiment according to the
     * command line args.
     */
    private void processOptions(String args[]) throws Exception {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        Vector<Project> instances = loadProjects(listMDGFiles(cmd));

        int repetitions = cmd.hasOption("repetitions")
                ? Integer.parseInt(cmd.getOptionValue("repetitions"))
                : 20;

        Experiment<CouplingProblem, Project> experiment = new Experiment<>();
        experiment.runCycles(cmd.getOptionValue("output"), new ClusteringProblemBuilder(), instances, repetitions);
    }

    /*
     * Load CMS projects from a list of files.
     */
    private Vector<Project> loadProjects(List<String> files) throws Exception {
        Vector<Project> projects = new Vector<>();
        for(String file : files) {
            CDAFlatReader reader = new CDAFlatReader(file);
            reader.execute(file);
            projects.add(reader.getProject());
        }
        return projects;
    }

    /*
     * List the selected MDG files, from the
     * command line arguments.
     */
    private Vector<String> listMDGFiles(CommandLine cmd) throws Exception {
        String path = cmd.hasOption("input-dir") ? cmd.getOptionValue("input-dir") : cmd.getOptionValue("input-file");
        File file = new File(path);

        List<String> mdgFiles = new ArrayList<>();

        if(file.exists() && file.isDirectory()) {
            mdgFiles = Arrays.stream(file.list())
                        .filter(f -> f.endsWith("mdg"))
                        .collect(Collectors.toList());
        }
        else if(file.exists()) {
            mdgFiles.add(file.getAbsolutePath());
        }
        else {
            throw new Exception("The path " + path + " does not exist");
        }
        return new Vector<>(mdgFiles);
    }

    public void initOptions() {
        options = new Options();

        options.addOption(Option.builder()
                .longOpt("algorithm")
                .argName("algorithm")
                .desc("The algorithm that should be used. Default NSGAII")
                .argName("algorithm")
                .hasArg()
                .build());

        Option inputFile = Option.builder()
                .longOpt("input-file")
                .argName("input-file")
                .hasArg()
                .desc("Path to the MDG file")
                .build();

        Option inputDir = Option.builder()
                .longOpt("input-dir")
                .argName("input-dir")
                .hasArg()
                .desc("The path to a folder with MDG files")
                .build();

        OptionGroup inputGroup = new OptionGroup();

        inputGroup.setRequired(true);
        inputGroup.addOption(inputFile);
        inputGroup.addOption(inputDir);

        options.addOptionGroup(inputGroup);

        options.addOption(Option.builder()
                .longOpt("output")
                .argName("output")
                .hasArg()
                .required()
                .desc("Output file")
                .build());

        options.addOption(Option.builder()
                .longOpt("repetitions")
                .argName("repetitions")
                .required()
                .hasArg()
                .desc("Number of repetitions")
                .build());
    }
}
