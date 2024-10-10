cmake_minimum_required(VERSION 3.22.1)

project("stockfish")

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O3 -fno-exceptions -DNNUE_EMBEDDING_OFF -DUSE_PTHREADS -fPIE -s -flto=thin")

set(SOURCE_FILES
        Stockfish/benchmark.cpp
        Stockfish/bitboard.cpp
        Stockfish/evaluate.cpp
        Stockfish/main.cpp
        Stockfish/misc.cpp
        Stockfish/movegen.cpp
        Stockfish/movepick.cpp
        Stockfish/position.cpp
        Stockfish/search.cpp
        Stockfish/thread.cpp
        Stockfish/timeman.cpp
        Stockfish/tt.cpp
        Stockfish/uci.cpp
        Stockfish/ucioption.cpp
        Stockfish/tune.cpp
        Stockfish/syzygy/tbprobe.cpp
        Stockfish/nnue/nnue_misc.cpp
        Stockfish/nnue/features/half_ka_v2_hm.cpp
        Stockfish/nnue/network.cpp
        Stockfish/engine.cpp
        Stockfish/score.cpp
        Stockfish/memory.cpp
)

set(HEADERS_FILES
        Stockfish/benchmark.h
        Stockfish/bitboard.h
        Stockfish/evaluate.h
        Stockfish/misc.h
        Stockfish/movegen.h
        Stockfish/movepick.h
        Stockfish/nnue/nnue_misc.h
        Stockfish/nnue/features/half_ka_v2_hm.h
        Stockfish/nnue/layers/affine_transform.h
        Stockfish/nnue/layers/affine_transform_sparse_input.h
        Stockfish/nnue/layers/clipped_relu.h
        Stockfish/nnue/layers/simd.h
        Stockfish/nnue/layers/sqr_clipped_relu.h
        Stockfish/nnue/nnue_accumulator.h
        Stockfish/nnue/nnue_architecture.h
        Stockfish/nnue/nnue_common.h
        Stockfish/nnue/nnue_feature_transformer.h
        Stockfish/position.h
        Stockfish/search.h
        Stockfish/syzygy/tbprobe.h
        Stockfish/thread.h
        Stockfish/thread_win32_osx.h
        Stockfish/timeman.h
        Stockfish/tt.h
        Stockfish/tune.h
        Stockfish/types.h
        Stockfish/uci.h
        Stockfish/ucioption.h
        Stockfish/perft.h
        Stockfish/nnue/network.h
        Stockfish/engine.h
        Stockfish/score.h
        Stockfish/numa.h
        Stockfish/memory.h
)

add_executable(
        ${CMAKE_PROJECT_NAME}
        ${SOURCE_FILES}
        ${HEADERS_FILES}
)

target_include_directories(${CMAKE_PROJECT_NAME} PRIVATE ${CMAKE_SOURCE_DIR}/Stockfish/syzygy)

