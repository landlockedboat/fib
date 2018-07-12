How to generate the datasets:

  For this example we'll use rawtext and rawperm as the input folders for filter.out and permgen.out respectively and filteredtext as output folder for both.

  1) Using filter.out

    filter.out eliminates all non-standard characters (diacritic accents, special regional characters, etc) from a given number of files placed on the folder we give to him.

    example:
      ./filter.out rawtext/ filteredtext/

  2) Using permgen.out

    permgen.out generates a random permutation of the words (each separated by a newline in the input files) from a given number of files placed on the folder we give to him. The number of permutations to be done over each file can be set using the -n flag.

    example:
      ./permgen.out rawperm/ filtered/

rawtext:
  Contains files tat can be used as input for filter.out.
rawperm:
  Contains files tat can be used as input for permgen.out.
filteredtext:
  Contains files outputted by filter.out and permgen.out
