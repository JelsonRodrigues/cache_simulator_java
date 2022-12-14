package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import main.errors.WrongReplacementPolicy;
import main.replacement_policies.Fifo;
import main.replacement_policies.Lfu;
import main.replacement_policies.Lru;
import main.replacement_policies.Mru;
import main.replacement_policies.Random;

public class Main {
    public static final int NUMBER_OF_NEEDED_ARGUMENTS = 6;
    public static final int ADRESS_SIZE = 32;
    public static void main(String[] args) throws Exception {
        Arguments arguments = new Arguments(args);
        
        Cache cache_l1;

        if (arguments.replacement.equals("R")) {
            cache_l1 = new Random(arguments.nsets, arguments.assoc, arguments.bsize);
        }
        else if (arguments.replacement.equals("L")) {
            cache_l1 = new Lru(arguments.nsets, arguments.assoc, arguments.bsize);
        }
        else if (arguments.replacement.equals("F")) {
            cache_l1 = new Fifo(arguments.nsets, arguments.assoc, arguments.bsize);
        }
        else if (arguments.replacement.equals("LFU")){
            cache_l1 = new Lfu(arguments.nsets, arguments.assoc, arguments.bsize);
        }
        else if (arguments.replacement.equals("MRU")){
            cache_l1 = new Mru(arguments.nsets, arguments.assoc, arguments.bsize);
        }
        else {
            throw new WrongReplacementPolicy("The replacement policy " + arguments.replacement + " is not implemented!!!");
        }

        File file = new File(arguments.filename);
        InputStream inputStream = new FileInputStream(file);

        byte[] buffer = new byte[4];
        
        while (inputStream.read(buffer) == buffer.length){
            int address = ByteBuffer.wrap(buffer).getInt();
            cache_l1.read(address);
        }
        inputStream.close();
        
        if (arguments.output_format == false) {
            System.out.println("Politica de substituicao: " + arguments.replacement);
        }
        System.out.println(cache_l1.getHistory().getFormattedHistory(arguments.output_format));
    }
    
}
