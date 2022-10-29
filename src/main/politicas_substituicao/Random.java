package main.politicas_substituicao;

import java.util.ArrayList;
import java.util.random.RandomGenerator;

import main.Cache;
import main.MissType;
import main.errors.NotPowerOf2;

public class Random extends Cache {
    private class Line {
        public boolean valid;
        public int tag;

        public Line(){
            this.valid = false;
            this.tag = 0;
        }
    }

    private class Set {
        public ArrayList<Line> positions;
        public int last_index;
        public boolean is_full;

        public Set(int assoc) {
            this.last_index = 0;
            this.is_full = false;
            
            this.positions = new ArrayList<>(assoc);

            for (int c = 0; c < assoc; c++) {
                positions.add(new Line());
            }
        }
    }

    private ArrayList<Set> memory;
    private int total_bytes_used;
    private int max_capacity;


    public Random(int nsets, int assoc, int bsize) throws NotPowerOf2 {
        super(nsets, assoc, bsize);
        this.total_bytes_used = 0;
        this.max_capacity = nsets * assoc * bsize;
        this.memory = new ArrayList<>(nsets);
        for (int c = 0; c < nsets; c++){
            this.memory.add(new Set(assoc));
        }
    }

    @Override
    public void read(int address) {
        int index_of_adress = this.getIndex(address);
        int tag_of_adress = this.getTag(address);

        Set set = memory.get(index_of_adress);

        int assoc = this.getAssoc();

        // Procura o valor na cache
        for (int c = 0; c < assoc; c++) {
            Line line = set.positions.get(c);
            if (line.valid && line.tag == tag_of_adress) {
                this.getHistory().addHit();
                return;
            }
        }

        // Miss
        int position = 0;
        // Tratamento da falta e atualização do histórico de acesso
        if (set.is_full == false) {
            // Compulsorio
            position = set.last_index;
            set.last_index += 1;
            if (set.last_index >= set.positions.size()) {
                set.is_full = true;
            }

            this.total_bytes_used += this.getBsize();

            this.getHistory().addMiss(MissType.COMPULSORY);
        }
        else {
            // Posicao a ser substituida
            position = RandomGenerator.getDefault().nextInt(assoc) % this.getAssoc();

            if (this.is_full()) {
                // Capacidade
                this.getHistory().addMiss(MissType.CAPACITY);
            }
            else {
                // Conflito
                this.getHistory().addMiss(MissType.CONFLICT);
            }
        }

        Line line = set.positions.get(position); 
        line.tag = tag_of_adress;
        line.valid = true;
    }

    private boolean is_full() {
        return max_capacity == this.total_bytes_used;
    }
}

 