package com.alphatica.genotick.instructions;

import com.alphatica.genotick.processor.Processor;

import java.io.Serializable;

public class ChangeOfColumn extends RegRegInstruction implements Serializable{
    private static final long serialVersionUID = 7118840738537901113L;

    @SuppressWarnings("unused")
    public ChangeOfColumn() {}

    public ChangeOfColumn(ChangeOfColumn instruction) {
        this.setRegister1(instruction.getRegister1());
        this.setRegister2(instruction.getRegister2());
    }

    @Override
    public void executeOn(Processor processor) {
        processor.execute(this);
    }

    @Override
    public Instruction copy() {
        return new ChangeOfColumn(this);
    }
}
