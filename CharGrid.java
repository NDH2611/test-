public class CharGrid {
    private char[][] grid;

    public CharGrid(char[][] grid) {
        this.grid = grid;
    }

    public int charArea(char ch) {
        int m = grid.length;
        int n = grid[0].length;
        int minRow = m, maxRow = -1;
        int minCol = n, maxCol = -1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == ch) {
                    if (i < minRow) minRow = i;
                    if (i > maxRow) maxRow = i;
                    if (j < minCol) minCol = j;
                    if (j > maxCol) maxCol = j;
                }
            }
        }
        if (maxRow == -1) return 0;
        return (maxRow - minRow + 1) * (maxCol - minCol + 1);
    }

    public static void main(String[] args) {

        char[][] board = {
                {'a', 'b', 'c'},
                {'d', 'a', 'e'},
                {'f', 'g', 'a'}
        };
        CharGrid grid = new CharGrid(board);
        System.out.println(grid.charArea('a'));
        System.out.println(grid.charArea('b'));
        System.out.println(grid.charArea('z'));
    }

}
