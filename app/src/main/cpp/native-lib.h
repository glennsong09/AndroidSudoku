//
// Created by Glenn on 6/26/2019.
//

#ifndef PROJECTS2019_NATIVE_LIB_H
#define PROJECTS2019_NATIVE_LIB_H

#endif //PROJECTS2019_NATIVE_LIB_H

#pragma once
#include <string>
#include <vector>
#include <iostream>
using namespace std;

class Puzzle
{

public:

	Puzzle();
    	Puzzle(std::string input);
    	~Puzzle();

    	std::vector<std::vector<char> > sudoku_board;

    	void ConvertToVectorFromString(std::string input);

    	char GetSudokuBoardValue(int row_pos, int col_pos) const;

    	std::vector<int> FindEmptyPosition();

    	bool CheckNumbersAllowedInPositionBox(int row_position, int col_position, char to_check);

    	bool CheckNumbersAllowedInPositionHorizontalVertical(int row_position, int col_position, char to_check);

    	bool SolveBoard();

    	bool IsAssignedByInput(int row_position, int col_position);

    	friend ostream& operator << (ostream& os, const Puzzle& board);

    	friend istream& operator >> (istream& os, Puzzle& board);

    	std::vector<std::vector<char> >& GetSudokuBoard() { return sudoku_board; };

	extern "C" {
       JNIEXPORT jint  JNICALL Java_com_example_sudoku_Puzzle_doNativeAction( JNIEnv* env,jobject thiz,jcharArray gameData );
       }
};

