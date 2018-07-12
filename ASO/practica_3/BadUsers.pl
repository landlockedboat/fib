#!/usr/bin/perl
$numArgs = @ARGV;
$p=0;
$usage="Usage: BadUsers.pl [-p] \n";

if ( $numArgs != 0)
{
    if ( $numArgs == 1 )
    {
        # si el primer argument es -p 
        if ($ARGV[0] eq "-p") 
        {
            # activem el flag p            
            $p=1;                 
        }
        else 
        {
            die $usage; 
        }
    }
    else 
    {
        die $usage; 
    }    
}

$pass_db_file="/etc/passwd";

open $FILE, $pass_db_file or die "no es pot obrir el fitxer $pass_db_file: $!";
# llegir tot el fitxer de password
@password_db= <$FILE>;
close $FILE;

foreach $user_line (@password_db)
{
    # eliminar el salt de línia
    chop $user_line;

    @fields = split(':', $user_line);
    $user_id = $fields[0];
    $user_home = $fields[5];

    if ( -d $user_home )
    {
        $comand=sprintf("find %s -type f -user %s | wc -l", $user_home, $user_id);
        $find_out=`$comand`;  
        chop $find_out;
    } 
    else 
    { 
        $find_out = 0; 
    }
    if ($find_out == 0)
    {
        $invalid_users{$user_id} = "invalid";
    }
}

if ( $p == 1 ){
    @process_list=`ps aux --no-headers`;
    foreach $process_list_line (@process_list) {
        chomp $process_list_line;
        @fields_proc = split(" ", $process_list_line);
        $user_proc = @fields_proc[0];
        delete($invalid_users{$user_proc});
    }
}

foreach $user_inv_id (sort keys%invalid_users)
{
    print "$user_inv_id\n";
}
