/**
 * This file is part of PlaytimePlugin.
 * 
 * PlaytimePlugin is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * PlaytimePlugin is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with PlaytimePlugin. If not, see <https://www.gnu.org/licenses/>.
 *
 * @author Ben Staehle
 * @date 5/15/22
 */

package org.ben.plugin;
import java.io.File;

import org.ben.plugin.io.ParseFile;
import org.ben.plugin.io.PlayerTime;
import org.ben.plugin.io.WriteFile;

public class Tester {
    public static void main(String[] args) throws Exception {
        try {
            File f = WriteFile.createFile();
            PlayerTime p = new PlayerTime("test_player", "uuid", f);
            // PlayerTime p2 = new PlayerTime("test_player2", "uuid2", f);
            // PlayerTime p3 = new PlayerTime("test_player3", "uuid3", f);
            WriteFile.newEntry(p);
            // WriteFile.newEntry(p2);
             p.login();
            Thread.sleep(2000);
            WriteFile.updateEntry(p);
            PlayerTime temp = new PlayerTime("test_player");
            System.out.println(ParseFile.existsInFile(f, temp));
            //PlayerTime test = ParseFile.getPlayerTimeInFile(f, "test_player");
            // WriteFile.newEntry(p3);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
