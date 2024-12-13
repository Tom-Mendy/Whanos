cmake_minimum_required(VERSION 3.10)

project(
  compiled-app
  VERSION 0.1
  DESCRIPTION "c++ project"
  LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_executable(compiled-app
  app/hello.cpp
)

add_custom_command(
  TARGET compiled-app
  POST_BUILD
  COMMAND ${CMAKE_COMMAND} -E copy $<TARGET_FILE:compiled-app> ${CMAKE_SOURCE_DIR})