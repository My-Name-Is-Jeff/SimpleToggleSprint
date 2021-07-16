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
package mynameisjeff.simpletogglesprint.commands

import gg.essential.api.EssentialAPI
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import mynameisjeff.simpletogglesprint.core.Config

object SimpleToggleSprintCommand : Command("simpletogglesprint") {

    override val commandAliases: Set<Alias>
        get() = setOf(Alias("togglesprint"))

    @DefaultHandler
    fun execCommand() {
        EssentialAPI.getGuiUtil().openScreen(Config.gui())
    }
}