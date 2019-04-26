package com.instructure.bp.codelabs.exception

import java.lang.RuntimeException

class TodoItemNotFound(val id: String): RuntimeException()