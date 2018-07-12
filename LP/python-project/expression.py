#!/usr/bin/python3


def eval_conjunction(key, eval_string):
    for k in key:
        if not eval_key(k, eval_string):
            return False
    return True


def eval_disjunction(key, eval_string):
    for k in key:
        if eval_key(k, eval_string):
            return True
    return False


def eval_word(key, eval_string):
    return key in eval_string


def eval_key(key, eval_string):
    if isinstance(key, list):
        return eval_conjunction(key, eval_string)
    elif isinstance(key, tuple):
        return eval_disjunction(key, eval_string)
    elif isinstance(key, str):
        return eval_word(key, eval_string)
    else:
        print ("non valid key:")
        print (key)
