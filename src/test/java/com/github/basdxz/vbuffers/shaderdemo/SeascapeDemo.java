package com.github.basdxz.vbuffers.shaderdemo;

import lombok.*;
import org.joml.*;

import java.lang.Math;


/*

 * "Seascape" by Alexander Alekseev aka TDM - 2014
 * License Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * Contact: tdmaav@gmail.com

 Yoinked from https://www.shadertoy.com/view/Ms2SD1
 */
public class SeascapeDemo {
    private static final float iTime = 0F;

    //const int NUM_STEPS = 8;
    //const float PI	 	= 3.141592;
    //const float EPSILON	= 1e-3;
    //#define EPSILON_NRM (0.1 / iResolution.x)
    //#define AA
    private static final int NUM_STEPS = 8;
    private static final float PI = 3.141592f;
    private static final float EPSILON = 1e-3f;
    private static final float EPSILON_NRM = 0.1f / 800;
    private static final boolean AA = false;

    //// sea
    //const int ITER_GEOMETRY = 3;
    //const int ITER_FRAGMENT = 5;
    //const float SEA_HEIGHT = 0.6;
    //const float SEA_CHOPPY = 4.0;
    //const float SEA_SPEED = 0.8;
    //const float SEA_FREQ = 0.16;
    //const vec3 SEA_BASE = vec3(0.0,0.09,0.18);
    //const vec3 SEA_WATER_COLOR = vec3(0.8,0.9,0.6)*0.6;
    //#define SEA_TIME (1.0 + iTime * SEA_SPEED)
    //const mat2 octave_m = mat2(1.6,1.2,-1.2,1.6);
    private static final int ITER_GEOMETRY = 3;
    private static final int ITER_FRAGMENT = 5;
    private static final float SEA_HEIGHT = 0.6f;
    private static final float SEA_CHOPPY = 4.0f;
    private static final float SEA_SPEED = 0.8f;
    private static final float SEA_FREQ = 0.16f;
    private static final Vector3f SEA_BASE = new Vector3f(0.0f, 0.09f, 0.18f);
    private static final Vector3f SEA_WATER_COLOR = new Vector3f(0.8f, 0.9f, 0.6f).mul(0.6f);
    private static final float SEA_TIME = 1.0f + 0 * SEA_SPEED;
    private static final Matrix2f octave_m = new Matrix2f(1.6f, 1.2f, -1.2f, 1.6f);

    //// math
    //mat3 fromEuler(vec3 ang) {
    //	vec2 a1 = vec2(sin(ang.x),cos(ang.x));
    //    vec2 a2 = vec2(sin(ang.y),cos(ang.y));
    //    vec2 a3 = vec2(sin(ang.z),cos(ang.z));
    //    mat3 m;
    //    m[0] = vec3(a1.y*a3.y+a1.x*a2.x*a3.x,a1.y*a2.x*a3.x+a3.y*a1.x,-a2.y*a3.x);
    //	m[1] = vec3(-a2.y*a1.x,a1.y*a2.y,a2.x);
    //	m[2] = vec3(a3.y*a1.x*a2.x+a1.y*a3.x,a1.x*a3.x-a1.y*a3.y*a2.x,a2.y*a3.y);
    //	return m;
    //}
    private static Matrix3f fromEuler(final Vector3fc ang) {
        Vector2f a1 = new Vector2f((float) Math.sin(ang.x()), (float) Math.cos(ang.x()));
        Vector2f a2 = new Vector2f((float) Math.sin(ang.y()), (float) Math.cos(ang.y()));
        Vector2f a3 = new Vector2f((float) Math.sin(ang.z()), (float) Math.cos(ang.z()));
        Matrix3f m = new Matrix3f();
        m.m00 = a1.y * a3.y + a1.x * a2.x * a3.x;
        m.m01 = a1.y * a2.x * a3.x + a3.y * a1.x;
        m.m02 = -a2.y * a3.x;
        m.m10 = -a2.y * a1.x;
        m.m11 = a1.y * a2.y;
        m.m12 = a2.x;
        m.m20 = a3.y * a1.x * a2.x + a1.y * a3.x;
        m.m21 = a1.x * a3.x - a1.y * a3.y * a2.x;
        m.m22 = a2.y * a3.y;
        return m;
    }

    //float hash( vec2 p ) {
    //	float h = dot(p,vec2(127.1,311.7));
    //    return fract(sin(h)*43758.5453123);
    //}
    private static float hash(final Vector2fc p) {
        float h = p.dot(new Vector2f(127.1f, 311.7f));
        return (float) (h - Math.floor(h));
    }

    //float noise( in vec2 p ) {
    //    vec2 i = floor( p );
    //    vec2 f = fract( p );
    //	vec2 u = f*f*(3.0-2.0*f);
    //    return -1.0+2.0*mix( mix( hash( i + vec2(0.0,0.0) ),
    //                     hash( i + vec2(1.0,0.0) ), u.x),
    //                mix( hash( i + vec2(0.0,1.0) ),
    //                     hash( i + vec2(1.0,1.0) ), u.x), u.y);
    //}
    private static float noise(final Vector2fc p) {
        Vector2f i = new Vector2f((float) Math.floor(p.x()), (float) Math.floor(p.y()));
        Vector2f f = new Vector2f(p.x() - i.x, p.y() - i.y);
        Vector2f u = new Vector2f(f.x * f.x * (3.0f - 2.0f * f.x), f.y * f.y * (3.0f - 2.0f * f.y));
        return -1.0f + 2.0f * (float) (MathUtils.mix(MathUtils.mix(hash(new Vector2f(i.x + 0.0f, i.y + 0.0f)),
                                                                   hash(new Vector2f(i.x + 1.0f, i.y + 0.0f)), u.x),
                                                     MathUtils.mix(hash(new Vector2f(i.x + 0.0f, i.y + 1.0f)),
                                                                   hash(new Vector2f(i.x + 1.0f, i.y + 1.0f)), u.x), u.y));
    }

    //// lighting
    //float diffuse(vec3 n,vec3 l,float p) {
    //    return pow(dot(n,l) * 0.4 + 0.6,p);
    //}
    private static float diffuse(final Vector3fc n, final Vector3fc l, float p) {
        return (float) Math.pow(n.dot(l) * 0.4f + 0.6f, p);
    }

    //float specular(vec3 n,vec3 l,vec3 e,float s) {
    //    float nrm = (s + 8.0) / (PI * 8.0);
    //    return pow(max(dot(reflect(e,n),l),0.0),s) * nrm;
    //}
    private static float specular(final Vector3fc n, final Vector3fc l, final Vector3fc e, float s) {
        float nrm = (s + 8.0f) / (PI * 8.0f);
        return (float) Math.pow(Math.max(n.reflect(e, new Vector3f()).dot(l), 0.0f), s) * nrm;
    }

    //// sky
    //vec3 getSkyColor(vec3 e) {
    //    e.y = (max(e.y,0.0)*0.8+0.2)*0.8;
    //    return vec3(pow(1.0-e.y,2.0), 1.0-e.y, 0.6+(1.0-e.y)*0.4) * 1.1;
    //}
    private static Vector3f getSkyColor(final Vector3fc e) {
        val y = (Math.max(e.y(), 0.0f) * 0.8f + 0.2f) * 0.8f;
        return new Vector3f((float) Math.pow(1.0f - y, 2.0f), 1.0f - y, 0.6f + (1.0f - y) * 0.4f).mul(1.1f);
    }

    //// sea
    //float sea_octave(vec2 uv, float choppy) {
    //    uv += noise(uv);
    //    vec2 wv = 1.0-abs(sin(uv));
    //    vec2 swv = abs(cos(uv));
    //    wv = mix(wv,swv,wv);
    //    return pow(1.0-pow(wv.x * wv.y,0.65),choppy);
    //}
    private static float sea_octave(final Vector2fc uv, float choppy) {
        val newUV = uv.add(new Vector2f(noise(uv)), new Vector2f());
        Vector2f wv = new Vector2f(1.0f - Math.abs((float) Math.sin(newUV.x)), 1.0f - Math.abs((float) Math.sin(newUV.y)));
        Vector2f swv = new Vector2f(Math.abs((float) Math.cos(newUV.x)), Math.abs((float) Math.cos(newUV.y)));
        wv = new Vector2f(MathUtils.mix(wv.x, swv.x, wv.x), MathUtils.mix(wv.y, swv.y, wv.y));
        return (float) Math.pow(1.0f - Math.pow(wv.x * wv.y, 0.65f), choppy);
    }

    //float map(vec3 p) {
    //    float freq = SEA_FREQ;
    //    float amp = SEA_HEIGHT;
    //    float choppy = SEA_CHOPPY;
    //    vec2 uv = p.xz; uv.x *= 0.75;
    //
    //    float d, h = 0.0;
    //    for(int i = 0; i < ITER_GEOMETRY; i++) {
    //    	d = sea_octave((uv+SEA_TIME)*freq,choppy);
    //    	d += sea_octave((uv-SEA_TIME)*freq,choppy);
    //        h += d * amp;
    //    	uv *= octave_m; freq *= 1.9; amp *= 0.22;
    //        choppy = mix(choppy,1.0,0.2);
    //    }
    //    return p.y - h;
    //}
    private static float map(final Vector3fc p) {
        float freq = SEA_FREQ;
        float amp = SEA_HEIGHT;
        float choppy = SEA_CHOPPY;
        Vector2f uv = new Vector2f(p.x(), p.z());
        uv.x *= 0.75f;

        float d, h = 0.0f;
        for (int i = 0; i < ITER_GEOMETRY; i++) {
            d = sea_octave(new Vector2f((uv.x + SEA_TIME) * freq, (uv.y + SEA_TIME) * freq), choppy);
            d += sea_octave(new Vector2f((uv.x - SEA_TIME) * freq, (uv.y - SEA_TIME) * freq), choppy);
            h += d * amp;
            uv.mul(octave_m);
            freq *= 1.9f;
            amp *= 0.22f;
            choppy = MathUtils.mix(choppy, 1.0f, 0.2f);
        }
        return p.y() - h;
    }

    //float map_detailed(vec3 p) {
    //    float freq = SEA_FREQ;
    //    float amp = SEA_HEIGHT;
    //    float choppy = SEA_CHOPPY;
    //    vec2 uv = p.xz; uv.x *= 0.75;
    //
    //    float d, h = 0.0;
    //    for(int i = 0; i < ITER_FRAGMENT; i++) {
    //    	d = sea_octave((uv+SEA_TIME)*freq,choppy);
    //    	d += sea_octave((uv-SEA_TIME)*freq,choppy);
    //        h += d * amp;
    //    	uv *= octave_m; freq *= 1.9; amp *= 0.22;
    //        choppy = mix(choppy,1.0,0.2);
    //    }
    //    return p.y - h;
    //}
    private static float map_detailed(final Vector3fc p) {
        float freq = SEA_FREQ;
        float amp = SEA_HEIGHT;
        float choppy = SEA_CHOPPY;
        Vector2f uv = new Vector2f(p.x(), p.z());
        uv.x *= 0.75f;

        float d, h = 0.0f;
        for (int i = 0; i < ITER_FRAGMENT; i++) {
            d = sea_octave(new Vector2f((uv.x + SEA_TIME) * freq, (uv.y + SEA_TIME) * freq), choppy);
            d += sea_octave(new Vector2f((uv.x - SEA_TIME) * freq, (uv.y - SEA_TIME) * freq), choppy);
            h += d * amp;
            uv.mul(octave_m);
            freq *= 1.9f;
            amp *= 0.22f;
            choppy = MathUtils.mix(choppy, 1.0f, 0.2f);
        }
        return p.y() - h;
    }

    //vec3 getSeaColor(vec3 p, vec3 n, vec3 l, vec3 eye, vec3 dist) {
    //    float fresnel = clamp(1.0 - dot(n,-eye), 0.0, 1.0);
    //    fresnel = pow(fresnel,3.0) * 0.5;
    //
    //    vec3 reflected = getSkyColor(reflect(eye,n));
    //    vec3 refracted = SEA_BASE + diffuse(n,l,80.0) * SEA_WATER_COLOR * 0.12;
    //
    //    vec3 color = mix(refracted,reflected,fresnel);
    //
    //    float atten = max(1.0 - dot(dist,dist) * 0.001, 0.0);
    //    color += SEA_WATER_COLOR * (p.y - SEA_HEIGHT) * 0.18 * atten;
    //
    //    color += vec3(specular(n,l,eye,60.0));
    //
    //    return color;
    //}
    private static Vector3f getSeaColor(final Vector3fc p, final Vector3fc n, final Vector3fc l, final Vector3fc eye, final Vector3fc dist) {
        float fresnel = MathUtils.clamp(1.0f - n.dot(eye.negate(new Vector3f())), 0.0f, 1.0f);
        fresnel = (float) Math.pow(fresnel, 3.0f) * 0.5f;

        Vector3f reflected = getSkyColor(eye.reflect(n, new Vector3f()));
        Vector3f refracted = new Vector3f(SEA_BASE).add(new Vector3f(diffuse(n, l, 80.0f)).mul(SEA_WATER_COLOR).mul(0.12f));

        Vector3f color = new Vector3f(refracted).lerp(reflected, fresnel);

        float atten = Math.max(1.0f - dist.lengthSquared() * 0.001f, 0.0f);
        color.add(new Vector3f(SEA_WATER_COLOR).mul(p.y() - SEA_HEIGHT).mul(0.18f).mul(atten));

        color.add(new Vector3f(specular(n, l, eye, 60.0f)));

        return color;
    }

    //// tracing
    //vec3 getNormal(vec3 p, float eps) {
    //    vec3 n;
    //    n.y = map_detailed(p);
    //    n.x = map_detailed(vec3(p.x+eps,p.y,p.z)) - n.y;
    //    n.z = map_detailed(vec3(p.x,p.y,p.z+eps)) - n.y;
    //    n.y = eps;
    //    return normalize(n);
    //}
    private static Vector3f getNormal(final Vector3fc p, float eps) {
        Vector3f n = new Vector3f();
        n.y = map_detailed(p);
        n.x = map_detailed(new Vector3f(p.x() + eps, p.y(), p.z())) - n.y;
        n.z = map_detailed(new Vector3f(p.x(), p.y(), p.z() + eps)) - n.y;
        n.y = eps;
        return n.normalize();
    }

    //float heightMapTracing(vec3 ori, vec3 dir, out vec3 p) {
    //    float tm = 0.0;
    //    float tx = 1000.0;
    //    float hx = map(ori + dir * tx);
    //    if(hx > 0.0) {
    //        p = ori + dir * tx;
    //        return tx;
    //    }
    //    float hm = map(ori + dir * tm);
    //    float tmid = 0.0;
    //    for(int i = 0; i < NUM_STEPS; i++) {
    //        tmid = mix(tm,tx, hm/(hm-hx));
    //        p = ori + dir * tmid;
    //    	float hmid = map(p);
    //		if(hmid < 0.0) {
    //        	tx = tmid;
    //            hx = hmid;
    //        } else {
    //            tm = tmid;
    //            hm = hmid;
    //        }
    //    }
    //    return tmid;
    //}
    private static float heightMapTracing(final Vector3fc ori, final Vector3fc dir, final Vector3f p) {
        float tm = 0.0f;
        float tx = 1000.0f;
        float hx = map(ori.add(dir.mul(tx, new Vector3f()), new Vector3f()));
        if (hx > 0.0f) {
            p.set(ori).add(dir.mul(tx, new Vector3f()));
            return tx;
        }
        float hm = map(ori.add(dir.mul(tm, new Vector3f()), new Vector3f()));
        float tmid = 0.0f;
        for (int i = 0; i < NUM_STEPS; i++) {
            tmid = MathUtils.mix(tm, tx, hm / (hm - hx));
            p.set(ori).add(dir.mul(tmid, new Vector3f()));
            float hmid = map(p);
            if (hmid < 0.0f) {
                tx = tmid;
                hx = hmid;
            } else {
                tm = tmid;
                hm = hmid;
            }
        }
        return tmid;
    }

    //vec3 getPixel(in vec2 coord, float time) {
    //    vec2 uv = coord / iResolution.xy;
    //    uv = uv * 2.0 - 1.0;
    //    uv.x *= iResolution.x / iResolution.y;
    //
    //    // ray
    //    vec3 ang = vec3(sin(time*3.0)*0.1,sin(time)*0.2+0.3,time);
    //    vec3 ori = vec3(0.0,3.5,time*5.0);
    //    vec3 dir = normalize(vec3(uv.xy,-2.0)); dir.z += length(uv) * 0.14;
    //    dir = normalize(dir) * fromEuler(ang);
    //
    //    // tracing
    //    vec3 p;
    //    heightMapTracing(ori,dir,p);
    //    vec3 dist = p - ori;
    //    vec3 n = getNormal(p, dot(dist,dist) * EPSILON_NRM);
    //    vec3 light = normalize(vec3(0.0,1.0,0.8));
    //
    //    // color
    //    return mix(
    //        getSkyColor(dir),
    //        getSeaColor(p,n,light,dir,dist),
    //    	pow(smoothstep(0.0,-0.02,dir.y),0.2));
    //}
    private static Vector3f getPixel(Vector2fc coord, float time) {
        val uv = new Vector2f(coord).mul(2F).sub(1F, 1F).mul(1F, -1F);

        Vector3f ang = new Vector3f((float) Math.sin(time * 3.0f) * 0.1f, (float) Math.sin(time) * 0.2f + 0.3f, time);
        Vector3f ori = new Vector3f(0.0f, 3.5f, time * 5.0f);
        Vector3f dir = new Vector3f(uv.x, uv.y, -2.0f).normalize();
        dir.z += uv.length() * 0.14f;
        dir.normalize().mul(fromEuler(ang));

        // tracing
        Vector3f p = new Vector3f();
//        heightMapTracing(ori, dir, p);
        Vector3f dist = new Vector3f(p).sub(ori);
        Vector3f n = getNormal(p, dist.lengthSquared() * EPSILON_NRM);
        Vector3f light = new Vector3f(0.0f, 1.0f, 0.8f).normalize();

        // color
        val skyColor = getSkyColor(dir);
        val seaColor = getSeaColor(p, n, light, dir, dist);

//        return new Vector3f(skyColor).lerp(seaColor, (float) Math.pow(MathUtils.smoothstep(0.0f, -0.02f, dir.y), 0.2f));

        return new Vector3f(skyColor);
    }

    //void mainImage( out vec4 fragColor, in vec2 fragCoord ) {
    //    float time = iTime * 0.3 + iMouse.x*0.01;
    //
    //#ifdef AA
    //    vec3 color = vec3(0.0);
    //    for(int i = -1; i <= 1; i++) {
    //        for(int j = -1; j <= 1; j++) {
    //        	vec2 uv = fragCoord+vec2(i,j)/3.0;
    //    		color += getPixel(uv, time);
    //        }
    //    }
    //    color /= 9.0;
    //#else
    //    vec3 color = getPixel(fragCoord, time);
    //#endif
    //
    //    // post
    //	fragColor = vec4(pow(color,vec3(0.65)), 1.0);
    //}
    public static Vector3f ballerFragmentShader(Vector2fc uv) {
        float time = iTime * 0.3f + 0 * 0.01f;

        Vector3f color = new Vector3f();
        if (AA) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    Vector2f uv2 = new Vector2f(uv).add(i, j).div(3.0f);
                    color.add(getPixel(uv2, time));
                }
            }
            color.div(9.0f);
        } else {
            color = getPixel(uv, time);
        }

        // post
        color.x = (float) Math.pow(color.x, 0.65f);
        color.y = (float) Math.pow(color.y, 0.65f);
        color.z = (float) Math.pow(color.z, 0.65f);

        return color;
    }

    private static class MathUtils {
        public static float mix(float x, float y, float a) {
            return x * (1.0f - a) + y * a;
        }

        public static float clamp(float x, float y, float a) {
            return Math.max(y, Math.min(x, a));
        }

        //GLSL Smooth Step function
        public static double smoothstep(float v, float v1, float y) {
            float x = clamp((y - v) / (v1 - v), 0.0f, 1.0f);
            return x * x * (3.0f - 2.0f * x);
        }
    }
}
