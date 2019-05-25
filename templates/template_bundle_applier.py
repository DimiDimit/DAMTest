import argparse
import json
import os

from template_replacer import replace_all, replace_file, compile_replacement


def apply_bundle(bundle, replacements, prefix):
    for f in bundle:
        creplacements = dict(replacements)
        freplacements = f.get("replacements")
        if freplacements is not None:
            for k, v in freplacements.items():
                creplacements[k] = replace_all(v, replacements)
        replace_file(f["pattern"], creplacements.items(),
                     os.path.join(prefix if prefix is not None else "", replace_all(f["destination"], replacements)))


if __name__ == '__main__':
    argp = argparse.ArgumentParser(description="Apply template bundles.")
    argp.add_argument("bundle", help="bundle to apply")
    argp.add_argument("replacements", type=compile_replacement, nargs="*", help='replacements as "a=b"')
    argp.add_argument("-p", "--prefix", help="prefix for files in bundle")
    args = argp.parse_args()
    with open(args.bundle) as bundlef:
        bundle = json.load(bundlef)
    apply_bundle(bundle, args.replacements, args.prefix)
