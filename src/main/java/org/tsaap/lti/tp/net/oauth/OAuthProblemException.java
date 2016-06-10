/*
 * Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.tsaap.lti.tp.net.oauth;

import net.oauth.OAuth;
import net.oauth.OAuthException;

import java.util.HashMap;
import java.util.Map;

/**
 * An OAuth-related problem, described using a set of named parameters. One
 * parameter identifies the basic problem, and the others provide supplementary
 * diagnostic information. This can be used to capture information from a
 * response that conforms to the OAuth <a
 * href="http://wiki.oauth.net/ProblemReporting">Problem Reporting
 * extension</a>.
 *
 * @author John Kristian
 */
public class OAuthProblemException extends OAuthException {

    public static final String OAUTH_PROBLEM = "oauth_problem";
    /**
     * The name of a parameter whose value is the HTTP request.
     */
    public static final String HTTP_REQUEST = "HTTP request";
    /**
     * The name of a parameter whose value is the HTTP response.
     */
    public static final String HTTP_RESPONSE = "HTTP response";
    /**
     * The name of a parameter whose value is the HTTP resopnse status code.
     */
    public static final String HTTP_STATUS_CODE = "HTTP status";
    /**
     * The name of a parameter whose value is the response Location header.
     */
    public static final String HTTP_LOCATION = "Location";
    /**
     * The name of a parameter whose value is the OAuth signature base string.
     */
    public static final String SIGNATURE_BASE_STRING = OAuth.OAUTH_SIGNATURE + " base string";
    /**
     * The name of a parameter whose value is the request URL.
     */
    public static final String URL = "URL";

    public OAuthProblemException() {
    }

    public OAuthProblemException(String problem) {
        super(problem);
        if (problem != null) {
            parameters.put(OAUTH_PROBLEM, problem);
        }
    }

    private final Map<String, Object> parameters = new HashMap<String, Object>();

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        if (msg != null)
            return msg;
        msg = getProblem();
        if (msg != null)
            return msg;
        Object response = getParameters().get(HTTP_RESPONSE);
        if (response != null) {
            msg = response.toString();
            int eol = msg.indexOf("\n");
            if (eol < 0) {
                eol = msg.indexOf("\r");
            }
            if (eol >= 0) {
                msg = msg.substring(0, eol);
            }
            msg = msg.trim();
            if (msg.length() > 0) {
                return msg;
            }
        }
        response = getHttpStatusCode();
        if (response != null) {
            return HTTP_STATUS_CODE + " " + response;
        }
        return null;
    }

    public void setParameter(String name, Object value) {
        getParameters().put(name, value);
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public String getProblem() {
        return (String) getParameters().get(OAUTH_PROBLEM);
    }

    public int getHttpStatusCode() {
        Object code = getParameters().get(HTTP_STATUS_CODE);
        if (code == null) {
            return 200;
        } else if (code instanceof Number) { // the usual case
            return ((Number) code).intValue();
        } else {
            return Integer.parseInt(code.toString());
        }
    }

    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder(super.toString());
        try {
            final String eol = System.getProperty("line.separator", "\n");
            final Map<String, Object> parameters = getParameters();
            for (String key : new String[]{OAuth.Problems.OAUTH_PROBLEM_ADVICE, URL,
                    SIGNATURE_BASE_STRING}) {
                Object value = parameters.get(key);
                if (value != null)
                    s.append(eol + key + ": " + value);
            }
            Object msg = parameters.get(HTTP_REQUEST);
            if ((msg != null))
                s.append(eol + ">>>>>>>> " + HTTP_REQUEST + ":" + eol + msg);
            msg = parameters.get(HTTP_RESPONSE);
            if (msg != null) {
                s.append(eol + "<<<<<<<< " + HTTP_RESPONSE + ":" + eol + msg);
            } else {
                for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                    String key = parameter.getKey();
                    if (OAuth.Problems.OAUTH_PROBLEM_ADVICE.equals(key)
                            || URL.equals(key) || SIGNATURE_BASE_STRING.equals(key)
                            || HTTP_REQUEST.equals(key) || HTTP_RESPONSE.equals(key))
                        continue;
                    s.append(eol + key + ": " + parameter.getValue());
                }
            }
        } catch (Exception ignored) {
        }
        return s.toString();
    }

    private static final long serialVersionUID = 1L;

}