package edu.qc.seclass.replace;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;


public class Replace {

    private boolean validator;
    protected static boolean fileNotFound = false;
    protected Boolean toogle = false;
    protected Operation op = new Operation();
    protected ArrayList<String> toFrom = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();
    HashSet<String> commands = new HashSet<>();
    boolean usage = false;

    /**
     * Utility for backup if it already exists
     **/
    private void backupExists(File in) {
        System.err.println("Not performing replaceWithoutCommands for " + in.getName() + ": Backup file already exists");
    }

    public void setArgs(String [] args) {

        commands.add("-b");
        commands.add("-l");
        commands.add("-i");
        commands.add("-f");
        commands.add("--");

        int index = 0;
        for(String arg: args){
            Pattern p = Pattern.compile("[^a-zA-Z0-9]");
            boolean hasSpecialChar = p.matcher(arg).find();

            if(!hasSpecialChar){
                list.add(arg);
            }
            if(hasSpecialChar && !commands.contains(arg)) {
                list.add(arg);
            }

            if(arg.equals(""))
                list.add(arg);

            if(hasSpecialChar && op.isDDash && !arg.equals("--"))
                list.add(arg);

            if((arg.equals("-i") || arg.equals("-b")|| arg.equals("-l")|| arg.equals("-f")) && index <=1){
                op.operation.add(arg);
            }
            else if((arg.equals("-i") || arg.equals("-b")|| arg.equals("-l")|| arg.equals("-f")) && index <=3 & !op.isDDash){
                op.operation.add(arg);
                if(list.size() > 0) {
                    list.add(arg);
                    op.operation.remove(arg);
                }
            }

            if(arg.equals("--") && index > 1){
                break;
            }
            else if (arg.equals("--") && index <= 1) {
                op.isDDash = true;
            }
            index++;
        }

        if(list.size() > 2 && !list.contains(""))
            usage = true;
        index++;

        if(list.size() == 0){
            if(index < args.length)
                op.setFrom(args[index++]);
            if(index < args.length)
                op.setTo(args[index++]);
            for (int i = index; i < args.length; i++) {
                if(!args[i].equals("--"))
                    op.files.add(new File(args[i]));
            }
        }
        else {
            for (int i = index; i < args.length; i++) {
                op.files.add(new File(args[i]));
            }
        }

        if (list.size() > 0)
            op.setFrom(list.get(0));
        if (list.size() > 1)
            op.setTo(list.get(1));

        if(list.size() > 2 && list.size() % 2 == 0){
            for(int i = 0; i < list.size(); i+=2) {
                toFrom.add(list.get(i));
                toFrom.add(list.get(i+1));
            }
        }
        if(toFrom.size() > 2)
            usage = false;
        processor();
    }

    /**
     * Process which operations to use
     **/
    public void processor(){
        if(op.files.size() == 0 || op.from == null || op.to == null || op.from.equals("") || (op.to.equals("") && usage) || usage)
            usage();
        else {
            for (int i = 0; i < op.files.size(); i++) {

                if(op.operation.size() == 0){
                    noOperation(op.from, op.to, op.files, toFrom);
                }
                if (op.operation.contains("-b")) {
                    if(op.operation.contains("-i") && op.operation.size() <=2)
                        toogle = true;
                    operationB(op.from, op.to, op.files, toogle, toFrom);
                    toogle = false;
                }
                if (op.operation.contains("-i") && op.operation.size() == 1) {
                    operationI(op.from, op.to, op.files, toFrom);
                }
                if (op.operation.contains("-f")) {
                    if(op.operation.contains("-i"))
                        toogle = true;
                    operationF(op.from, op.to, op.files, toogle, toFrom);
                    toogle = false;
                }
                if (op.operation.contains("-l")) {
                    if(op.operation.contains("-i"))
                        toogle = true;
                    operationL(op.from, op.to, op.files, toogle, toFrom);
                    toogle = false;
                }
                break;
            }
        }
    }

    /**
     * Utility to get content from file
     **/
    public String getFileContent(File file) {
        Charset charset = StandardCharsets.UTF_8;
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(file.getPath())), charset);
        } catch (IOException e) {

            fileNotFound(file.getName());
        }
        return content;
    }

    /**
     * Error Utility
     **/
    public void fileNotFound(String filePath) {
        fileNotFound =true;
        File file = new File(filePath);
        System.err.println("File " + file.getName() + " not found");
        file.delete();
    }

    /**
     * Validator utility
     **/
    public void usage() {
        if(!validator) {
            validator =true;
            System.err.println("Usage: Replace [-b] [-f] [-l] [-i] <from> <to> -- " + "<filename> [<filename>]*" );
        }
    }

    /**
     * Operation -f
     **/
    public void operationF(String from, String to, ArrayList<File> files, boolean toogle, ArrayList<String> toFrom){

        for(int i = 0; i < files.size(); i++){
            String content = getFileContent(files.get(i));

            if(toFrom.size() <= 2) {
                if (toogle)
                    content = content.replaceFirst("(?i)" + from, to);
                else
                    content = content.replaceFirst(from, to);
            }
            else{
                for(int j = 0; j < toFrom.size(); j+=2){
                    if (toogle)
                        content = content.replaceFirst("(?i)" + toFrom.get(j), toFrom.get(j+1));
                    else
                        content = content.replaceFirst(toFrom.get(j), toFrom.get(j+1));
                }
            }
            writToFile(files.get(i), content);
        }
    }

    /**
     * Operation -l
     **/
    public void operationL(String from, String to, ArrayList<File> files, boolean toogle, ArrayList<String> toFrom){

        for(int i = 0; i < files.size(); i++){
            String content = getFileContent(files.get(i));
            String[] content_list = content.split(System.getProperty("line.separator"));
            int l = content_list.length;

            if(toFrom.size() <= 2) {
                if (toogle)
                    content_list[l- 1] = content_list[l - 1].replaceAll("(?i)" + from, to);
                else
                    content_list[l - 1] = content_list[l - 1].replaceAll(from, to);
                }
            else{
                for(int j = 0; j < toFrom.size(); j+=2){
                    if (toogle)
                        content_list[l - 1] = content_list[l - 1].replaceAll("(?i)" + toFrom.get(j),toFrom.get(j+1));
                    else
                        content_list[l- 1] = content_list[l- 1].replaceAll(toFrom.get(j), toFrom.get(j+1));
                }
            }
            content = String.join("\n", content_list);
            writToFile(files.get(i), content);
        }

    }

    /**
     * Operation -b
     **/
    public void operationB(String from, String to, ArrayList<File> files, boolean toogle, ArrayList<String> toFrom){

        for(int i = 0; i < files.size(); i++) {

            if(Files.exists(Paths.get(files.get(i).getPath()+".bck"))){
                backupExist(files.get(i));
            }
            else {
                String content = getFileContent(files.get(i));
                File backupFile = new File(files.get(i).getPath() + ".bck");
                try {
                    FileWriter backupFileWriter = new FileWriter(backupFile);
                    backupFileWriter.write(content);
                    backupFileWriter.close();
                } catch (java.io.IOException e) {
                }
            }
        }
        if(toogle){
            operationI(from, to, files, toFrom);
        }
        else{
            if(!op.operation.contains("-f") && !op.operation.contains("-l"))
                noOperation(from, to, files, toFrom);
        }
    }

    /**
     * Operation -i
     **/
    public void operationI(String from, String to, ArrayList<File> files, ArrayList<String> toFrom) {

        for(int i = 0; i < files.size(); i++) {
            String content = getFileContent(files.get(i));
            if (content == null)
                System.out.print("File " + files.get(i).getName() + " not found");
            else {
                if (toFrom.size() <= 2)
                    content = content.replaceAll("(?i)" + from, to);
                else {
                    for (int j = 0; j < toFrom.size(); j += 2) {
                        content = content.replaceAll("(?i)" + toFrom.get(j), toFrom.get(j+1));
                    }
                }
                writToFile(files.get(i), content);
            }
        }
    }

    /**
     * Case of no given operation
     **/
    public void noOperation(String from, String to, ArrayList<File> files, ArrayList<String> toFrom) {

        if(!from.equals("[")) {
            for (int i = 0; i < files.size(); i++) {
                String content = getFileContent(files.get(i));
                if(content == null) {
                    System.out.print("File " + files.get(i).getName() + " not found");
                }
                else {
                    if (toFrom.size() <= 2) {
                        try {
                            content = content.replaceAll(from, to);
                        } catch (java.lang.NullPointerException e) {}
                    }
                    else {
                        for (int j = 0; j < toFrom.size(); j += 2) {
                            content = content.replaceAll(toFrom.get(j), toFrom.get(j+1));
                        }
                    }
                    writToFile(files.get(i), content);
                }
            }
        }
    }

    /**
     *Check if backup already exists
     **/
    private void backupExist(File in) {

        System.err.println("Not performing replace for " + in.getName() + ": Backup file already exists");

    }

    /**
     * Util to write to file
     **/
    public void writToFile(File file, String content){
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print(content);
            writer.close();
        } catch (java.io.FileNotFoundException e) {
        }
    }
}