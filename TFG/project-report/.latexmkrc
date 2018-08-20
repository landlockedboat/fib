use File::Basename;

add_cus_dep('glo', 'gls', 0, 'run_makeglossaries');
add_cus_dep('acn', 'acr', 0, 'run_makeglossaries');

sub run_makeglossaries {
	$dir = dirname($_[0]);
	$file = basename($_[0]);

	if ( $silent ) {
		system "makeglossaries -d '$dir' -q '$file'";
	}
	else {
		system "makeglossaries -d '$dir' '$file'";
	};
}

# pdf previewer
$pdf_previewer = "start evince";
$pdf_update_method = 0;
$out_dir = 'out';
$aux_dir = 'out';
