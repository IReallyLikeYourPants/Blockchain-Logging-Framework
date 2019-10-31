package au.csiro.data61.aap.spec;

import au.csiro.data61.aap.state.ProgramState;

/**
 * Instruction
 */
public abstract class Instruction {
    private final Scope enclosingScope;

    protected Instruction(Scope enclosingScope) {
        this.enclosingScope = enclosingScope;
    }

    public Scope getEnclosingScope() {
        return this.enclosingScope;
    }
    
    // TODO: instructions need a way to report error handling or abort execution   
    
    public abstract void execute(ProgramState state);
    
}