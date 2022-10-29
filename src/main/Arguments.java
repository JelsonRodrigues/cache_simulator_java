package main;
import main.errors.WrongNumberOfArgs;

public class Arguments {
    public final int nsets;
    public final int assoc;
    public final int bsize;
    public final String replacement;
    public final boolean output_format;
    public final String filename;

    public Arguments(String[] args) throws WrongNumberOfArgs {
        if (args.length != Main.NUMBER_OF_NEEDED_ARGUMENTS) {
            throw new WrongNumberOfArgs("O número de argumentos lidos é diferente do número de argumentos necessários, lido " + args.length + ", necessarios: " + Main.NUMBER_OF_NEEDED_ARGUMENTS);
        }

        this.nsets = Integer.parseInt(args[0]);
        this.bsize = Integer.parseInt(args[1]);
        this.assoc = Integer.parseInt(args[2]);
        this.replacement = args[3];
        this.output_format = Integer.parseInt(args[4]) == 1 ? true : false;
        this.filename = args[5];
    }

}
