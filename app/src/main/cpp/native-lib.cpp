#include "native-lib.h"
#include "jni.h"
#include <vector>
#include <algorithm>
#include <string>

using std::vector;
using namespace std;

#define HEIGHT 9
#define WIDTH 9

const int kSizeOfBox = 3;

//References logic from https://medium.com/@george.seif94/solving-sudoku-using-a-simple-search-algorithm-3ac44857fee8

Puzzle::Puzzle()
{
}

Puzzle::Puzzle(std::string input)
{
    ConvertToVectorFromString(input);
}

Puzzle::~Puzzle()
{
}

//Changes the original sudoku string into a 2D vector.
void Puzzle::ConvertToVectorFromString(string input) {
    sudoku_board.resize(HEIGHT);
    for (int i = 0; i < HEIGHT; ++i) {
        sudoku_board[i].resize(WIDTH);
    }
    int increment_string_pos = 0;
    for (int row = 0; row < WIDTH; row++) {
        for (int col = 0; col < HEIGHT; col++) {
            sudoku_board[col][row] = input[increment_string_pos];
            increment_string_pos++;
        }
    }
}

//Returns the value at a specific position.
char Puzzle::GetSudokuBoardValue(int row_pos, int col_pos) const {
    return sudoku_board[row_pos][col_pos];
}

//Checks if the point on the board needs to be filled in or not.
bool Puzzle::IsAssignedByInput(int row_position, int col_position) {
    if (sudoku_board[row_position][col_position] == '_') {
        return false;
    }
    else {
        return true;
    }
}

//Finds an empty position on the board.
std::vector<int> Puzzle::FindEmptyPosition() {
    std::vector<int> empty_position;
    for (int i = 0; i < WIDTH; i++) {
        for (int j = 0; j < HEIGHT; j++) {
            if (IsAssignedByInput(i, j)) {
                continue;
            }
            else {
                empty_position = { i, j };
                return empty_position;
            }

        }
    }
    return empty_position;
}

//Finds if a number can be found along its row/column.
bool Puzzle::CheckNumbersAllowedInPositionHorizontalVertical(int row_position, int col_position, char to_check) {
    bool allowed_horizontal = true;
    bool allowed_vertical = true;
    for (int row = 0; row < WIDTH; row++) {
        if (sudoku_board[row][col_position] == to_check) {
            allowed_vertical = false;
        }
    }
    for (int col = 0; col < HEIGHT; col++) {
        if (sudoku_board[row_position][col] == to_check) {
            allowed_horizontal = false;
        }
    }
    return allowed_horizontal && allowed_vertical;
}

//Finds if a number can be found in its box.
bool Puzzle::CheckNumbersAllowedInPositionBox(int row_position, int col_position, char to_check) {
    bool allowed_box = true;
    int box_start_row_position = (row_position / kSizeOfBox) * kSizeOfBox;
    int box_start_col_position = (col_position / kSizeOfBox) * kSizeOfBox;
    for (int row = box_start_row_position; row < box_start_row_position + kSizeOfBox; row++) {
        for (int col = box_start_col_position; col < box_start_col_position + kSizeOfBox; col++) {
            if (sudoku_board[row][col] == to_check) {
                allowed_box = false;
            }
        }
    }
    return allowed_box;
}

//Uses the other methods to try to solve a sudoku puzzle.
bool Puzzle::SolveBoard() {
    std::vector<int> to_try = FindEmptyPosition();
    if (to_try.size() <= 0) {
        return true;
    }
    int row = to_try[0];
    int col = to_try[1];
    for (char num = '1'; num <= '9'; num++) {
        if (CheckNumbersAllowedInPositionHorizontalVertical(row, col, num) && CheckNumbersAllowedInPositionBox(row, col, num)) {
            sudoku_board[row][col] = num;
            if (SolveBoard()) {
                return true;
            }
            sudoku_board[row][col] = '_';
        }
    }
    return false;
}

ostream& operator << (ostream& os, const Puzzle& board) {
    for (int i = 0; i < HEIGHT; i++) {
        cout << "\n";
        for (int j = 0; j < WIDTH; j++) {
            os << board.GetSudokuBoardValue(i, j);
        }
        os << "\n";
    }
    return os;
}

istream& operator >> (istream& is, Puzzle& board) {
    string line;
    getline(is, line);
    while (line.length() <= 0 || line.find_first_of('#', 0) != std::string::npos) {
        getline(is, line);
    }
    board.ConvertToVectorFromString(line);
    return is;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_muninn_sudoku_NativeBridge_doNativeAction( JNIEnv* env, jobject thiz,jcharArray gameData ) {

    std::string board = "";
    jsize len = env->GetArrayLength(gameData);
    jchar *body = env->GetCharArrayElements(gameData,0);
    for (jint i = 0; i < len; i++){
        char ch = body[i];
        board += ch;
    }
    (env)->ReleaseCharArrayElements(gameData, body, 0);
    Puzzle p;
    p.ConvertToVectorFromString(board);

    if (p.SolveBoard()) {
        return 1;
    }
    return 0;
}
