#version 330 core

in vec4 frontColor;
in vec3 P, N, V;
out vec4 fragColor;

uniform vec4 matAmbient, matDiffuse, matSpecular;
uniform float matShininess;
uniform vec4 lightAmbient, lightDiffuse, lightSpecular, lightPosition;
uniform float time;
uniform bool rotate;

vec4 light(vec3 N, vec3 V, vec3 L)
{
	N=normalize(N);
	V=normalize(V);
	L=normalize(L);
	vec3 R = normalize( 2.0*dot(N,L)*N-L );
	float NdotL = max( 0.0, dot( N,L ) );
	float RdotV = max( 0.0, dot( R,V ) );
	float Idiff = NdotL;
	float Ispec = 0;
	if (NdotL>0) Ispec=pow( RdotV, matShininess );
	return
		 matAmbient * lightAmbient +
		matDiffuse * lightDiffuse * Idiff +
		matSpecular * lightSpecular * Ispec;
}

void main()
{
	vec3 light1 = vec3(0,10,0)-P;
	vec3 light2 = vec3(0,-10,0)-P;
	vec3 light3 = vec3(10,0,0)-P;
	vec3 light4 = vec3(-10,0,0)-P;

    fragColor = 
	vec4(0,.5,0,1)	*	light	(	N, 	V, 	light1) +
	vec4(.5,.5,0,1)	*	light	(	N, 	V, 	light2) +
	vec4(0,0,.5,1) 	*	light	(	N, 	V, 	light3) +
	vec4(.5,0,0,1)	*	light	(	N, 	V, 	light4);
}
