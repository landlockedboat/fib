#version 330 core
layout (location = 0) in vec3 vertex;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec3 color;
layout (location = 3) in vec2 texCoord;

out vec4 frontColor;

uniform mat4 modelViewProjectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat3 normalMatrix;
uniform vec4 matAmbient, matDiffuse, matSpecular;
uniform float matShininess;
uniform vec4 lightAmbient, lightDiffuse, lightSpecular, lightPosition;


vec4 light(vec3 N, vec3 H, vec3 L)
{
	N=normalize(N);
	H=normalize(H);
	L=normalize(L);
	float NdotL = max( 0.0, dot( N,L ) );
	float NdotH = max( 0.0, dot( N,H ) );
	float Idiff = 0;
	float Ispec = 0;
	if (NdotL>0) {
		Idiff = NdotL;
		Ispec=pow( NdotH, matShininess );
	}

	return
	 matAmbient * lightAmbient +
	matDiffuse * lightDiffuse * Idiff +
	matSpecular * lightSpecular * Ispec;
}


void main()
{
	vec3 P = (modelViewMatrix * vec4(vertex.xyz, 1.0)).xyz;
	vec3 N = (normalMatrix * normal);
	vec3 L = (lightPosition.xyz - P);
	frontColor = light(N, L-P, L);
	gl_Position = modelViewProjectionMatrix * vec4(vertex.xyz, 1.0);
}
