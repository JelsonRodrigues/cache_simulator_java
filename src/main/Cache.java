package main;
import main.errors.NotPowerOf2;

public abstract class Cache {
    private AccessHistory history;

    private int nsets;
    private int assoc;
    private int bsize;

    private int offset_size;
    private int index_size;
    private int tag_size;

    public Cache(int nsets, int assoc, int bsize) throws NotPowerOf2 {
        if (isPowerOf2(nsets) == false) {
            throw new NotPowerOf2("O valor de nsets deve ser uma potência de 2");
        }
        if (isPowerOf2(assoc) == false) {
            throw new NotPowerOf2("O valor de assoc deve ser uma potência de 2");
        }
        if (isPowerOf2(bsize) == false) {
            throw new NotPowerOf2("O valor de bsize deve ser uma potência de 2");
        }

        this.nsets = nsets;
        this.assoc = assoc;
        this.bsize = bsize;

        this.tag_size = calculateTagSize(nsets, bsize);
        this.offset_size = calculateOffsetSize(bsize);
        this.index_size = calculateIndexSize(nsets);

        this.history = new AccessHistory();
    }

    public AccessHistory getHistory() {
        return history;
    }

    public int getNsets() {
        return nsets;
    }

    public int getAssoc() {
        return assoc;
    }

    public int getBsize() {
        return bsize;
    }

    public int getOffset_size() {
        return offset_size;
    }

    public int getIndex_size() {
        return index_size;
    }

    public int getTag_size() {
        return tag_size;
    }

    private boolean isPowerOf2(int number) {
        /*
         * Em binário, cada bit do número corresponde a uma potência de 2
         * Se existir exatamente 0 ou 1 bits 1 significa que é uma potência de 2,
         * Se existir 2 ou mais bits 1 então o número é uma soma de potências de 2 e não
         * será o uma potência de 2 exata
         */
        return Integer.bitCount(1) <= 1;
    }

    public int getTag(int adress) {
        if (this.tag_size == 0) {
            return 0;
        } else {
            return adress >> (this.index_size + this.offset_size);
        }
    }

    public int getIndex(int adress) {
        if (this.index_size == 0) {
            return 0;
        } else {
            // Remove os bits do tag
            int index = adress << this.tag_size;

            // Desloca para a direita no número de bits do offset e do tag, deixando apenas
            // os bits do índice
            index >>>= (this.tag_size + this.offset_size);

            return index;

        }
    }

    public int getOffset(int adress) {
        if (offset_size == 0) {
            return 0;
        } else {
            // Deixa somente os bits do offset
            int offset = adress << (this.index_size + this.tag_size);
            
            // Desloca o offset para a direita
            offset >>>= (this.index_size + this.tag_size);

            return offset;
        }
    }

    private int calculateTagSize(int nsets, int bsize) {
        return Main.ADRESS_SIZE - calculateIndexSize(nsets) - calculateOffsetSize(bsize);
    }

    private int calculateOffsetSize(int bsize) {
        return (int) Cache.log2((double) bsize);
    }

    private int calculateIndexSize(int nsets){
        return (int) Cache.log2((double) nsets);
    }

    public static double log2(double number) {
        /*
         * Para calcular o log_base(numero) é possível fazendo
         * ln(numero) / ln(base)
         */
        double result = Math.log(number) / Math.log(2.0);
        return result;
    }

    public abstract void read(int address);
}