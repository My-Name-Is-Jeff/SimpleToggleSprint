/*
      SimpleToggleSprint
      Copyright (C) 2021  My-Name-Is-Jeff

      This program is free software: you can redistribute it and/or modify
      it under the terms of the GNU Affero General Public License as published by
      the Free Software Foundation, either version 3 of the License, or
      (at your option) any later version.

      This program is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU Affero General Public License for more details.

      You should have received a copy of the GNU Affero General Public License
      along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package mynameisjeff.simpletogglesprint.commands;

import club.sk1er.mods.core.ModCore;
import mynameisjeff.simpletogglesprint.forge.SimpleToggleSprint;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

public class SimpleToggleSprintCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "simpletogglesprint";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        ModCore.getInstance().getGuiHandler().open(SimpleToggleSprint.config.gui());
    }
}