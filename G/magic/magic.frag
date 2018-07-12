#version 330 core

in vec4 frontColor;
out vec4 fragColor;

in vec2 vtexCoord;

uniform sampler2D window;
uniform sampler2D interior1;
uniform sampler2D exterior2;

in vec3 eyeN;

void main()
{
	float a = texture(window, vtexCoord).a;
	if(a < 1.0){
		vec2 newTexCoord = vtexCoord+0.5*eyeN.xy;
		float a2 = texture(interior1, newTexCoord).a;
			if(a2 < 1.0){
				vec2 newNewTexCoord = vtexCoord+0.7*eyeN.xy;	
				fragColor = texture(exterior2, newNewTexCoord);
			}
			else
				fragColor = texture(interior1, newTexCoord);	
	}
	else
		fragColor = texture(window, vtexCoord);
	

}
