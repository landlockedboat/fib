function [ Ousp ] = unsalpepper( I )
    Ousp = uint8(nlfilter(I, [5 5], @uspFilter));
end