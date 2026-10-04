package org.clnlang.compreg;

import java.util.List;

import org.clnlang.compreg.commands.Command;
import org.clnlang.compreg.commands.CommandSequence;
import org.clnlang.compreg.commands.ReturnCommand;
import org.clnlang.compreg.commands.set.SetInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class MemoryTest {

    @Test
    void nestedFramesUseFreshBaselinesAndRestoreCallerOffsets() {
        Memory memory = new Memory();
        int globalOffset = memory.allocateInt();
        memory.setInt(globalOffset, 41);
        memory.setRootFrameLayout(memory.currentFrameLayout());

        Memory.FrameLayout frameLayout = new Memory.FrameLayout(2, 0, 0, 0);
        memory.pushFrame(frameLayout);
        memory.setInt(0, 10);
        memory.setInt(1, 11);

        memory.pushFrame(frameLayout);
        memory.setInt(0, 20);
        memory.popOffset();

        assertEquals(10, memory.getInt(0));
        assertEquals(11, memory.getInt(1));

        memory.popOffset();

        assertEquals(41, memory.getInt(globalOffset));
    }

    @Test
    void pushOffsetStartsAZeroBasedCompileTimeFrame() {
        Memory memory = new Memory();
        assertEquals(0, memory.allocateInt());

        memory.pushOffset();
        assertEquals(0, memory.allocateInt());
        assertEquals(0, memory.allocateDec());
        memory.popOffset();

        assertEquals(1, memory.allocateInt());
        assertEquals(0, memory.allocateDec());
    }

    @Test
    void returnStopsSequenceAndIsScopedToItsFunctionFrame() {
        Memory memory = new Memory();
        memory.pushFrame(new Memory.FrameLayout(1, 0, 0, 0));

        Command body = new CommandSequence(List.of(ReturnCommand.INSTANCE, new SetInt(0, 99)));
        body.execute(memory);

        assertEquals(true, memory.isReturnRequested());
        assertEquals(0, memory.getInt(0));

        memory.popOffset();

        assertEquals(false, memory.isReturnRequested());
    }
}
