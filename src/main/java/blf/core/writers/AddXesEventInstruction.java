package blf.core.writers;

import java.util.List;

import blf.core.ProgramState;
import blf.core.exceptions.ProgramException;
import blf.core.values.ValueAccessor;

/**
 * AddXesEventInstruction
 */
public class AddXesEventInstruction extends AddXesElementInstruction {
    private final ValueAccessor eid;

    public AddXesEventInstruction(ValueAccessor pid, ValueAccessor piid, ValueAccessor eid, List<XesParameter> parameters) {
        super(pid, piid, parameters);
        this.eid = eid;

    }

    @Override
    protected void startElement(XesWriter writer, ProgramState state, String pid, String piid) throws ProgramException {
        final String eId = this.getId(state, this.eid);
        XesWriter.startEvent(writer, pid, piid, eId);
    }

}
