import argparse


def compile_replacement(repl):
    s = repl.split("=", 1)
    if len(s) < 2:
        raise ValueError(s)
    return s


def replace(patternf, pattern, repl):
    return patternf.replace("@@" + pattern + "@@", repl)


def replace_all(pattern, replacements):
    for rep in replacements:
        pattern = replace(pattern, rep[0], rep[1])
    return pattern


def replace_file(patternf, replacements, destination):
    with open(patternf) as patternff:
        pattern = patternff.read()
    pattern = replace_all(pattern, replacements)
    with open(destination, "x") as fo:
        fo.write(pattern)


if __name__ == '__main__':
    argp = argparse.ArgumentParser(description="Replace templates' patterns (i.e. use the templates).")
    argp.add_argument("pattern", help="pattern file to replace")
    argp.add_argument("destination", help="destination for the new file")
    argp.add_argument("replacements", type=compile_replacement, nargs="*", help='replacements as "a=b"')
    args = argp.parse_args()
    replace_file(args.pattern, args.replacements, args.destination)
