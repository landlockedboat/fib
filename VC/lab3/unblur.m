function [ o ] = unblur( i , angle)
    PSF = fspecial('motion',15,angle);
    o = deconvwnr(i, PSF);
end

