package net.dimitrodam.forgetest

class MalformedConfigOptionException : RuntimeException {
	constructor() : super()
	constructor(option: String) : super("Malformed config option: $option")
	constructor(option: String, explanation: String) : super("Malformed config option: $option. $explanation")
}