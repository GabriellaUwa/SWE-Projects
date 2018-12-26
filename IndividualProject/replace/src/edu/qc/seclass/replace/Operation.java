package edu.qc.seclass.replace;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class Operation {

    protected HashSet operation = new HashSet();
    protected String from;
    protected String to;
    protected ArrayList<File> files =  new ArrayList<>();
    protected boolean isDDash = false;

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }
}

