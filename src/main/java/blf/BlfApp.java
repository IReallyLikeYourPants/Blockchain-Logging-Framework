package blf;

import blf.blockchains.algorand.check_Manifest;
import blf.util.RootListenerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlfApp {
    private static final int INDEX_CMD = 0;
    private static final int INDEX_PATH = 1;
    private static final String CMD_GENERATE = "generate";
    private static final String CMD_EXTRACT = "extract";
    private static final String CMD_VALIDATE = "validate";
    private static final Logger LOGGER = Logger.getLogger(BlfApp.class.getName());

    public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException, IOException {
        if (args.length < 2) {
            final String message = String.format(
                "Execution of ELF requires two arguments: [%s|%s|%s] <PATH_TO_SCRIPT>",
                CMD_GENERATE,
                CMD_EXTRACT,
                CMD_VALIDATE
            );
            LOGGER.log(Level.SEVERE, message);
            return;
        }

        final String filepath = args[INDEX_PATH];
        final File file = new File(filepath);
        if (!file.exists()) {
            final String message = String.format("Invalid file path: %s", filepath);
            LOGGER.log(Level.SEVERE, message);
            return;
        }

        final String command = args[INDEX_CMD].toLowerCase();
        switch (command) {
            case CMD_GENERATE:
                generate(filepath);
                break;
            case CMD_EXTRACT:
                extract(filepath);
                break;
            case CMD_VALIDATE:
                validate(filepath);
                break;
            default:
                final String message = String.format(
                    "Unsupported command. Must be %s, %s, or %s. But was: %s",
                    CMD_GENERATE,
                    CMD_EXTRACT,
                    CMD_VALIDATE,
                    command
                );
                LOGGER.log(Level.SEVERE, message);
                break;
        }
    }

    private static void generate(String filepath) {
        final Generator generator = new Generator();
        try {
            final String generatedCode = generator.generateLoggingFunctionality(filepath);
            LOGGER.log(Level.INFO, generatedCode);
        } catch (BcqlProcessingException ex) {
            ex.printStackTrace(System.err);
        }
    }

    private static void extract(String filepath) throws NoSuchAlgorithmException, InterruptedException, IOException {
        boolean Algo_true = false;
        boolean both = true;
        try {
            File check_type = new File(filepath);
            Scanner check_type2 = new Scanner(check_type);
            while (check_type2.hasNextLine()) {
                String data_check = check_type2.nextLine();
                if (data_check.contains("algorand") || data_check.contains("ALGORAND") || data_check.contains("ALGORAND")) {
                    Algo_true = true;
                    break;
                }
            }
            check_type2.close();
            if (Algo_true) {
                check_Manifest.startAlgo(filepath, both);
            } else {
                final Extractor extractor = new Extractor();
                try {
                    extractor.extractData(filepath);
                } catch (BcqlProcessingException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        } catch (RootListenerException e) {
            e.printStackTrace();
        }

    }

    private static void validate(String filepath) throws NoSuchAlgorithmException, InterruptedException, IOException {
        boolean one = false;
        boolean Algo_true = false;
        try {
            File check_type = new File(filepath);
            Scanner check_type2 = new Scanner(check_type);
            while (check_type2.hasNextLine()) {
                String data_check = check_type2.nextLine();
                if (data_check.contains("algorand") || data_check.contains("ALGORAND") || data_check.contains("ALGORAND")) {
                    Algo_true = true;
                    break;
                }
            }
            check_type2.close();
            if (Algo_true) {
                check_Manifest.startAlgo(filepath, one);
            } else {
                final Validator validator = new Validator();
                final List<BcqlProcessingError> errors = validator.analyzeScript(filepath);
                printValidationResult(errors);
            }
        } catch (BcqlProcessingException ex) {
            ex.printStackTrace(System.err);
        }
    }

    private static void printValidationResult(List<BcqlProcessingError> errors) {
        if (errors.isEmpty()) {
            LOGGER.log(Level.INFO, "The validation did not find errors.");
            return;
        }

        LOGGER.log(Level.WARNING, "The validation detected the following errors:");
        errors.forEach(System.out::println);
    }

}
