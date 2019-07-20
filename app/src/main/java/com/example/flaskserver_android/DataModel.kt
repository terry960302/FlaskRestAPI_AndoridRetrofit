package com.example.flaskserver_android


class DataModel {
    data class TodoInfo1(val todo1 : TaskInfo)
    data class TodoInfo2(val todo2 : TaskInfo)
    data class TodoInfo3(val todo3 : TaskInfo)

    data class TaskInfo(val task : String)

}